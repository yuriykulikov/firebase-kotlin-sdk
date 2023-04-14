/*
 * Copyright (c) 2023 GitLive Ltd.  Use of this source code is governed by the Apache 2.0 license.
 */

package dev.gitlive.firebase.database

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.database.ChildEvent.Type
import dev.gitlive.firebase.decode
import dev.gitlive.firebase.encode
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy

actual val Firebase.database by lazy {
  FirebaseDatabase(com.google.firebase.database.FirebaseDatabase.getInstance())
}

actual fun Firebase.database(url: String) =
    FirebaseDatabase(com.google.firebase.database.FirebaseDatabase.getInstance(url))

actual fun Firebase.database(app: FirebaseApp) =
    FirebaseDatabase(com.google.firebase.database.FirebaseDatabase.getInstance(app.admin))

actual fun Firebase.database(app: FirebaseApp, url: String) =
    FirebaseDatabase(com.google.firebase.database.FirebaseDatabase.getInstance(app.admin, url))

actual class FirebaseDatabase
internal constructor(val admin: com.google.firebase.database.FirebaseDatabase) {
  private var persistenceEnabled = true

  actual fun reference(path: String) =
      DatabaseReference(admin.getReference(path), persistenceEnabled)

  actual fun reference() = DatabaseReference(admin.reference, persistenceEnabled)

  actual fun setPersistenceEnabled(enabled: Boolean) =
      admin.setPersistenceEnabled(enabled).also { persistenceEnabled = enabled }

  actual fun setLoggingEnabled(enabled: Boolean) {
    // TODO admin.setLogLevel(Logger.Level.DEBUG.takeIf { enabled } ?: Logger.Level.NONE)
  }

  actual fun useEmulator(host: String, port: Int) {
    // TODO    admin.useEmulator(host, port)
  }
}

actual open class Query
internal constructor(
    open val admin: com.google.firebase.database.Query,
    val persistenceEnabled: Boolean
) {
  actual fun orderByKey() = Query(admin.orderByKey(), persistenceEnabled)

  actual fun orderByValue() = Query(admin.orderByValue(), persistenceEnabled)

  actual fun orderByChild(path: String) = Query(admin.orderByChild(path), persistenceEnabled)

  actual fun startAt(value: String, key: String?) =
      Query(admin.startAt(value, key), persistenceEnabled)

  actual fun startAt(value: Double, key: String?) =
      Query(admin.startAt(value, key), persistenceEnabled)

  actual fun startAt(value: Boolean, key: String?) =
      Query(admin.startAt(value, key), persistenceEnabled)

  actual fun endAt(value: String, key: String?) = Query(admin.endAt(value, key), persistenceEnabled)

  actual fun endAt(value: Double, key: String?) = Query(admin.endAt(value, key), persistenceEnabled)

  actual fun endAt(value: Boolean, key: String?) =
      Query(admin.endAt(value, key), persistenceEnabled)

  actual fun limitToFirst(limit: Int) = Query(admin.limitToFirst(limit), persistenceEnabled)

  actual fun limitToLast(limit: Int) = Query(admin.limitToLast(limit), persistenceEnabled)

  actual fun equalTo(value: String, key: String?) =
      Query(admin.equalTo(value, key), persistenceEnabled)

  actual fun equalTo(value: Double, key: String?) =
      Query(admin.equalTo(value, key), persistenceEnabled)

  actual fun equalTo(value: Boolean, key: String?) =
      Query(admin.equalTo(value, key), persistenceEnabled)

  actual val valueEvents: Flow<DataSnapshot>
    get() = callbackFlow {
      val listener =
          object : ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
              trySend(DataSnapshot(snapshot))
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
              close(error.toException())
            }
          }
      admin.addValueEventListener(listener)
      awaitClose { admin.removeEventListener(listener) }
    }
  actual fun childEvents(vararg types: Type): Flow<ChildEvent> = callbackFlow {
    val listener =
        object : ChildEventListener {

          val moved by lazy { types.contains(Type.MOVED) }
          override fun onChildMoved(
              snapshot: com.google.firebase.database.DataSnapshot,
              previousChildName: String?
          ) {
            if (moved) trySend(ChildEvent(DataSnapshot(snapshot), Type.MOVED, previousChildName))
          }

          val changed by lazy { types.contains(Type.CHANGED) }
          override fun onChildChanged(
              snapshot: com.google.firebase.database.DataSnapshot,
              previousChildName: String?
          ) {
            if (changed)
                trySend(ChildEvent(DataSnapshot(snapshot), Type.CHANGED, previousChildName))
          }

          val added by lazy { types.contains(Type.ADDED) }
          override fun onChildAdded(
              snapshot: com.google.firebase.database.DataSnapshot,
              previousChildName: String?
          ) {
            if (added) trySend(ChildEvent(DataSnapshot(snapshot), Type.ADDED, previousChildName))
          }

          val removed by lazy { types.contains(Type.REMOVED) }
          override fun onChildRemoved(snapshot: com.google.firebase.database.DataSnapshot) {
            if (removed) trySend(ChildEvent(DataSnapshot(snapshot), Type.REMOVED, null))
          }

          override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
            close(error.toException())
          }
        }
    admin.addChildEventListener(listener)
    awaitClose { admin.removeEventListener(listener) }
  }

  override fun toString() = admin.toString()
}

actual class DatabaseReference
internal constructor(
    override val admin: com.google.firebase.database.DatabaseReference,
    persistenceEnabled: Boolean
) : Query(admin, persistenceEnabled) {
  actual val key
    get() = admin.key
  actual fun child(path: String): DatabaseReference {
    return DatabaseReference(admin.child(path), persistenceEnabled)
  }

  actual fun push() = DatabaseReference(admin.push(), persistenceEnabled)
  actual fun onDisconnect() = OnDisconnect(admin.onDisconnect(), persistenceEnabled)

  actual suspend inline fun <reified T> setValue(value: T?, encodeDefaults: Boolean) =
      suspendCancellableCoroutine { continuation ->
        admin.setValue(encode(value, encodeDefaults)) { error, _ ->
          if (error != null) {
            continuation.resumeWithException(error.toException())
          } else {
            continuation.resume(Unit)
          }
        }
        //  .run { if(persistenceEnabled) await() else awaitWhileOnline() }
        //  .run { Unit }
      }

  actual suspend fun <T> setValue(
      strategy: SerializationStrategy<T>,
      value: T,
      encodeDefaults: Boolean
  ) = suspendCancellableCoroutine { continuation ->
    admin.setValue(encode(strategy, value, encodeDefaults)) { error, _ ->
        if (error != null) {
          continuation.resumeWithException(error.toException())
        } else {
          continuation.resume(Unit)
        }
      }
  }

  @Suppress("UNCHECKED_CAST")
  actual suspend fun updateChildren(update: Map<String, Any?>, encodeDefaults: Boolean) {
    suspendCancellableCoroutine { continuation ->
      admin.updateChildren(encode(update, encodeDefaults) as Map<String, Any?>) { error, _ ->
        if (error != null) {
          continuation.resumeWithException(error.toException())
        } else {
          continuation.resume(Unit)
        }
      }
    }
  }

  actual suspend fun removeValue() = suspendCancellableCoroutine { continuation ->
    admin.removeValue() { error, _ ->
      if (error != null) {
        continuation.resumeWithException(error.toException())
      } else {
        continuation.resume(Unit)
      }
    }
  }

  actual suspend fun <T> runTransaction(
      strategy: KSerializer<T>,
      transactionUpdate: (currentData: T) -> T
  ): DataSnapshot {
    val deferred = CompletableDeferred<DataSnapshot>()
    admin.runTransaction(
        object : Transaction.Handler {

          override fun doTransaction(currentData: MutableData): Transaction.Result {
            currentData.value = currentData.value?.let { transactionUpdate(decode(strategy, it)) }
            return Transaction.success(currentData)
          }

          override fun onComplete(
              error: DatabaseError?,
              committed: Boolean,
              snapshot: com.google.firebase.database.DataSnapshot?
          ) {
            if (error != null) {
              deferred.completeExceptionally(error.toException())
            } else {
              deferred.complete(DataSnapshot(snapshot!!))
            }
          }
        })
    return deferred.await()
  }
}

@Suppress("UNCHECKED_CAST")
actual class DataSnapshot
internal constructor(val admin: com.google.firebase.database.DataSnapshot) {

  actual val exists
    get() = admin.exists()

  actual val key
    get() = admin.key

  actual inline fun <reified T> value() = decode<T>(value = admin.value)

  actual fun <T> value(strategy: DeserializationStrategy<T>) = decode(strategy, admin.value)

  actual fun child(path: String) = DataSnapshot(admin.child(path))
  actual val children: Iterable<DataSnapshot>
    get() = admin.children.map { DataSnapshot(it) }
}

actual class OnDisconnect
internal constructor(
    val admin: com.google.firebase.database.OnDisconnect,
    val persistenceEnabled: Boolean
) {

  actual suspend fun removeValue() = suspendCancellableCoroutine { continuation ->
    admin.removeValue() { error, _ ->
      if (error != null) {
        continuation.resumeWithException(error.toException())
      } else {
        continuation.resume(Unit)
      }
    }
  }

  actual suspend fun cancel() = suspendCancellableCoroutine { continuation ->
    admin.cancel() { error, _ ->
      if (error != null) {
        continuation.resumeWithException(error.toException())
      } else {
        continuation.resume(Unit)
      }
    }
  }

  actual suspend inline fun <reified T> setValue(value: T, encodeDefaults: Boolean) {
    suspendCancellableCoroutine { continuation ->
      admin.setValue(encode(value, encodeDefaults)) { error, _ ->
        if (error != null) {
          continuation.resumeWithException(error.toException())
        } else {
          continuation.resume(Unit)
        }
      }
    }
  }

  actual suspend fun <T> setValue(
      strategy: SerializationStrategy<T>,
      value: T,
      encodeDefaults: Boolean
  ) = suspendCancellableCoroutine { continuation ->
    admin.setValue(encode(strategy, value, encodeDefaults)) { error, _ ->
        if (error != null) {
          continuation.resumeWithException(error.toException())
        } else {
          continuation.resume(Unit)
        }
      }
  }

  actual suspend fun updateChildren(update: Map<String, Any?>, encodeDefaults: Boolean) = suspendCancellableCoroutine { continuation ->
    admin.updateChildren(update.mapValues { (_, it) -> encode(it, encodeDefaults) }) { error, _ ->
        if (error != null) {
          continuation.resumeWithException(error.toException())
        } else {
          continuation.resume(Unit)
        }
      }
  }
}

actual typealias DatabaseException = com.google.firebase.database.DatabaseException
