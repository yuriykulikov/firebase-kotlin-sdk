<h1 align="left">Firebase Kotlin SDK <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/gitliveapp/firebase-kotlin-sdk?style=flat-square"> <a href="https://git.live"><img src="https://img.shields.io/badge/collaborate-on%20gitlive-blueviolet?style=flat-square"></a></h1>
<img align="left" width="75px" src="https://avatars2.githubusercontent.com/u/42865805?s=200&v=4"> 
  <b>Built and maintained with 🧡 by <a href="https://git.live">GitLive</a></b><br/>
  <i>Development teams merge faster with GitLive</i><br/>
<br/>
<br/>
The Firebase Kotlin SDK is a Kotlin-first SDK for Firebase. It's API is similar to the 
<a href="https://firebase.github.io/firebase-android-sdk/reference/kotlin/firebase-ktx/">Firebase Android SDK Kotlin Extensions</a> 
but also supports multiplatform projects, enabling you to use Firebase directly from your common source targeting 
<strong>iOS</strong>, <strong>Android</strong>, <strong>Desktop</strong> or <strong>Web</strong>, enabling the use of 
Firebase as a backend for <a href="https://www.jetbrains.com/lp/compose-multiplatform/">Compose Multiplatform</a>, for example.

## Available libraries

The following libraries are available for the various Firebase products.

| Service or Product	                                                             | Gradle Dependency                                                                                                              | API Coverage                                                                                                                                                             |
|---------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Authentication](https://firebase.google.com/docs/auth)                         | [`dev.gitlive:firebase-auth:1.10.4`](https://search.maven.org/artifact/dev.gitlive/firebase-auth/1.10.4/pom)                   | [![80%](https://img.shields.io/badge/-80%25-green?style=flat-square)](/firebase-auth/src/commonMain/kotlin/dev/gitlive/firebase/auth/auth.kt)                            |
| [Realtime Database](https://firebase.google.com/docs/database)                  | [`dev.gitlive:firebase-database:1.10.4`](https://search.maven.org/artifact/dev.gitlive/firebase-database/1.10.4/pom)           | [![70%](https://img.shields.io/badge/-70%25-orange?style=flat-square)](/firebase-database/src/commonMain/kotlin/dev/gitlive/firebase/database/database.kt)               |
| [Cloud Firestore](https://firebase.google.com/docs/firestore)                   | [`dev.gitlive:firebase-firestore:1.10.4`](https://search.maven.org/artifact/dev.gitlive/firebase-firestore/1.10.4/pom)         | [![60%](https://img.shields.io/badge/-60%25-orange?style=flat-square)](/firebase-firestore/src/commonMain/kotlin/dev/gitlive/firebase/firestore/firestore.kt)            |
| [Cloud Functions](https://firebase.google.com/docs/functions)                   | [`dev.gitlive:firebase-functions:1.10.4`](https://search.maven.org/artifact/dev.gitlive/firebase-functions/1.10.4/pom)         | [![80%](https://img.shields.io/badge/-80%25-green?style=flat-square)](/firebase-functions/src/commonMain/kotlin/dev/gitlive/firebase/functions/functions.kt)             |
| [Cloud Messaging](https://firebase.google.com/docs/cloud-messaging)             | [`dev.gitlive:firebase-messaging:1.10.4`](https://search.maven.org/artifact/dev.gitlive/firebase-messaging/1.10.4/pom)         | ![0%](https://img.shields.io/badge/-0%25-lightgrey?style=flat-square)                                                                                                    |
| [Cloud Storage](https://firebase.google.com/docs/storage)                       | [`dev.gitlive:firebase-storage:1.10.4`](https://search.maven.org/artifact/dev.gitlive/firebase-storage/1.10.4/pom)             | [![40%](https://img.shields.io/badge/-40%25-orange?style=flat-square)](/firebase-storage/src/commonMain/kotlin/dev/gitlive/firebase/storage/storage.kt)                  |
| [Installations](https://firebase.google.com/docs/projects/manage-installations) | [`dev.gitlive:firebase-installations:1.10.4`](https://search.maven.org/artifact/dev.gitlive/firebase-installations/1.10.4/pom) | [![90%](https://img.shields.io/badge/-90%25-green?style=flat-square)](/firebase-installations/src/commonMain/kotlin/dev/gitlive/firebase/installations/installations.kt) |
| [Remote Config](https://firebase.google.com/docs/remote-config)                 | [`dev.gitlive:firebase-config:1.10.4`](https://search.maven.org/artifact/dev.gitlive/firebase-config/1.10.4/pom)               | [![20%](https://img.shields.io/badge/-20%25-orange?style=flat-square)](/firebase-config/src/commonMain/kotlin/dev/gitlive/firebase/remoteconfig/FirebaseRemoteConfig.kt) |
| [Performance](https://firebase.google.com/docs/perf-mon)                        | [`dev.gitlive:firebase-perf:1.10.4`](https://search.maven.org/artifact/dev.gitlive/firebase-perf/1.10.4/pom)                   | [![1%](https://img.shields.io/badge/-1%25-orange?style=flat-square)](/firebase-perf/src/commonMain/kotlin/dev/gitlive/firebase/perf/performance.kt)                      |
| [Crashlytics](https://firebase.google.com/docs/crashlytics)                     | [`dev.gitlive:firebase-crashlytics:1.10.4`](https://search.maven.org/artifact/dev.gitlive/firebase-crashlytics/1.10.4/pom)     | [![80%](https://img.shields.io/badge/-1%25-orange?style=flat-square)](/firebase-crashlytics/src/commonMain/kotlin/dev/gitlive/firebase/crashlytics/crashlytics.kt)       |

Is the Firebase library or API you need missing? [Create an issue](https://github.com/GitLiveApp/firebase-kotlin-sdk/issues/new?labels=API+coverage&template=increase-api-coverage.md&title=Add+%5Bclass+name%5D.%5Bfunction+name%5D+to+%5Blibrary+name%5D+for+%5Bplatform+names%5D) to request additional API coverage or be awesome and [submit a PR](https://github.com/GitLiveApp/firebase-kotlin-sdk/fork)

## Kotlin-first design

Unlike the Kotlin Extensions for the Firebase Android SDK this project does not extend a Java based SDK so we get the full power of Kotlin including coroutines and serialization!

<h3><a href="https://kotlinlang.org/docs/tutorials/coroutines/async-programming.html#coroutines">Suspending functions</a></h3>

Asynchronous operations that return a single or no value are represented by suspending functions in the SDK instead of callbacks, listeners or OS specific types such as [Task](https://developer.android.com/reference/com/google/android/play/core/tasks/Task), for example:

```kotlin
suspend fun signInWithCustomToken(token: String): AuthResult
```

It is important to remember that unlike a callback based API, wating for suspending functions to complete is implicit and so if you don't want to wait for the result you can `launch` a new coroutine:

```kotlin
//TODO don't use GlobalScope
GlobalScope.launch {
  Firebase.auth.signOut()
}
```

<h3><a href="https://kotlinlang.org/docs/reference/coroutines/flow.html">Flows</a></h3>

Asynchronous streams of values are represented by Flows in the SDK instead of repeatedly invoked callbacks or listeners, for example:

```kotlin
val snapshots: Flow<DocumentSnapshot>
```

The flows are cold, which means a new listener is added every time a terminal operator is applied to the resulting flow. A buffer with the [default size](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/-channel/-b-u-f-f-e-r-e-d.html) is used to buffer values received from the listener, use the [`buffer` operator](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/buffer.html) on the flow to specify a user-defined value and to control what happens when data is produced faster than consumed, i.e. to control the back-pressure behavior. Often you are only interested in the latest value received, in this case you can use the [`conflate` operator](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/conflate.html) to disable buffering.

The listener is removed once the flow [completes](https://kotlinlang.org/docs/reference/coroutines/flow.html#flow-completion) or is [cancelled](https://kotlinlang.org/docs/reference/coroutines/flow.html#flow-cancellation).

<h3><a href="https://github.com/Kotlin/kotlinx.serialization">Serialization</a></h3>

The official Firebase SDKs use different platform-specific ways to support writing data with and without custom classes in [Cloud Firestore](https://firebase.google.com/docs/firestore/manage-data/add-data#custom_objects), [Realtime Database](https://firebase.google.com/docs/database/android/read-and-write#basic_write) and [Functions](https://firebase.google.com/docs/functions/callable).

The Firebase Kotlin SDK uses Kotlin serialization to read and write custom classes to Firebase. To use Kotlin serialization in your project add the plugin to your gradle file:

```groovy
plugins {
    kotlin("multiplatform") version "1.8.21" // or kotlin("jvm") or any other kotlin plugin
    kotlin("plugin.serialization") version "1.8.21"
}
```

Then mark you custom classes `@Serializable`:

```kotlin
@Serializable
data class City(val name: String)
```

Instances of these classes can now be passed [along with their serializer](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serializers.md#introduction-to-serializers) to the SDK:

```kotlin
db.collection("cities").document("LA").set(City.serializer(), city, encodeDefaults = true)
```

The `encodeDefaults` parameter is optional and defaults to `true`, set this to false to omit writing optional properties if they are equal to theirs default values.
Using [@EncodeDefault](https://kotlinlang.org/api/kotlinx.serialization/kotlinx-serialization-core/kotlinx.serialization/-encode-default/) on properties is a recommended way to locally override the behavior set with `encodeDefaults`.

You can also omit the serializer but this is discouraged due to a [current limitation on Kotlin/JS and Kotlin/Native](https://github.com/Kotlin/kotlinx.serialization/issues/1116#issuecomment-704342452)

<h4><a href="https://firebase.google.com/docs/firestore/manage-data/add-data#server_timestamp">Server Timestamp</a></h3>

[Firestore](https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/FieldValue?hl=en#serverTimestamp()) and the [Realtime Database](https://firebase.google.com/docs/reference/android/com/google/firebase/database/ServerValue#TIMESTAMP) provide a sentinel value you can use to set a field in your document to a server timestamp. So you can use these values in custom classes:

```kotlin
@Serializable
data class Post(
    // In case using Realtime Database.
    val timestamp = ServerValue.TIMESTAMP,
    // In case using Cloud Firestore.
    val timestamp: Timestamp = Timestamp.ServerTimestamp,
    // or
    val alternativeTimestamp = FieldValue.serverTimestamp,
    // or
    @Serializable(with = DoubleAsTimestampSerializer::class),
    val doubleTimestamp: Double = DoubleAsTimestampSerializer.serverTimestamp
)
```

In addition `firebase-firestore` provides [GeoPoint] and [DocumentReference] classes which allow persisting
geo points and document references in a native way:

```kotlin
@Serializable
data class PointOfInterest(
    val reference: DocumentReference, 
    val location: GeoPoint
)
val document = PointOfInterest(
    reference = Firebase.firestore.collection("foo").document("bar"),
    location = GeoPoint(51.939, 4.506)
)
```

<h4>Polymorphic serialization (sealed classes)</h4>

This sdk will handle polymorphic serialization automatically if you have a sealed class and its children marked as `Serializable`. It will include a `type` property that will be used to discriminate which child class is the serialized.

You can change this `type` property by using the `@FirebaseClassDiscrminator` annotation in the parent sealed class:

```kotlin
@Serializable
@FirebaseClassDiscriminator("class")
sealed class Parent {
    @Serializable
    @SerialName("child")
    data class Child(
        val property: Boolean
    ) : Parent
}
```

In combination with a `SerialName` specified for the child class, you have full control over the serialized data. In this case it will be:

```json
{
  "class": "child",
  "property": true
}
```

<h3><a href="https://kotlinlang.org/docs/reference/functions.html#default-arguments">Default arguments</a></h3>

To reduce boilerplate, default arguments are used in the places where the Firebase Android SDK employs the builder pattern:
```kotlin
UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
        .setDisplayName("Jane Q. User")
        .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
        .build()

user.updateProfile(profileUpdates)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User profile updated.")
                }
            }
        })

//...becomes...

user.updateProfile(displayName = "state", photoURL = "CA")
```



<h3><a href="https://kotlinlang.org/docs/functions.html#infix-notation">Infix notation</a></h3>

To improve readability and reduce boilerplate for functions such as the Cloud Firestore query operators are built with infix notation:

```kotlin
citiesRef.whereEqualTo("state", "CA")
citiesRef.whereArrayContains("regions", "west_coast")
citiesRef.where(Filter.and(
    Filter.equalTo("state", "CA"),
    Filter.or(
        Filter.equalTo("capital", true),
        Filter.greaterThanOrEqualTo("population", 1000000)
    )
))

//...becomes...

citiesRef.where { "state" equalTo "CA" }
citiesRef.where { "regions" contains "west_coast" }
citiesRef.where {
    all(
        "state" equalTo "CA",
        any(
            "capital" equalTo true,
            "population" greaterThanOrEqualTo 1000000
        )
    )
}
```

<h3><a href="https://kotlinlang.org/docs/reference/operator-overloading.html">Operator overloading</a></h3>

In cases where it makes sense, such as Firebase Functions HTTPS Callable, operator overloading is used:

```kotlin
    val addMessage = functions.getHttpsCallable("addMessage")
    //In the official android Firebase SDK this would be addMessage.call(...)
    addMessage(mapOf("text" to text, "push" to true))
```

## Multiplatform

The Firebase Kotlin SDK provides a common API to access Firebase for projects targeting *iOS*, *Android* and *JS* meaning you can use Firebase directly in your common code. Under the hood, the SDK achieves this by binding to the respective official Firebase SDK for each supported platform.

It uses the <a href="https://github.com/GitLiveApp/firebase-java-sdk">Firebase Java SDK</a> to support the JVM target.

### Accessing the underlying Firebase SDK

In some cases you might want to access the underlying official Firebase SDK in platform specific code, for example when the common API is missing the functionality you need. For this purpose each class in the SDK has `android`, `ios` and `js` properties which holds the  equivalent object of the underlying official Firebase SDK. 

These properties are only accessible from the equivalent target's source set. For example to disable persistence in Cloud Firestore on Android you can write the following in your Android specific code (e.g. `androidMain` or `androidTest`):

```kotlin
  Firebase.firestore.android.firestoreSettings = FirebaseFirestoreSettings.Builder(Firebase.firestore.android.firestoreSettings)
          .setPersistenceEnabled(false)
          .build()
```

## Contributing
If you'd like to contribute to this project then you can fork this repository. 
You can build and test the project locally.
1. Open the project in IntelliJ IDEA.
2. Install cocoapods via `sudo gem install -n /usr/local/bin cocoapods`
3. Install the GitLive plugin into IntelliJ
4. After a gradle sync then run `publishToMavenLocal`
