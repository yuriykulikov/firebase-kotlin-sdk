/*
 * Copyright (c) 2023 GitLive Ltd.  Use of this source code is governed by the Apache 2.0 license.
 */

package dev.gitlive.firebase

import com.google.firebase.ErrorCode

actual typealias FirebaseException = com.google.firebase.FirebaseException

open class FirebaseAdminException(
    errorCode: ErrorCode,
    message: String,
    cause: Throwable?,
): com.google.firebase.FirebaseException(errorCode, message, cause)

actual typealias FirebaseNetworkException = FirebaseAdminException

actual typealias FirebaseTooManyRequestsException = FirebaseAdminException

actual typealias FirebaseApiNotAvailableException = FirebaseAdminException

actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(com.google.firebase.FirebaseApp.getInstance())

actual fun Firebase.app(name: String): FirebaseApp =
    FirebaseApp(com.google.firebase.FirebaseApp.getInstance(name))

actual fun Firebase.initialize(context: Any?): FirebaseApp? {
    TODO()
    // return com.google.firebase.FirebaseApp.initializeApp(context as String)?.let { FirebaseApp(it) }
}

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions, name: String): FirebaseApp {
    TODO()
    // return FirebaseApp(com.google.firebase.FirebaseApp.initializeApp(context as String, options.toAdmin(), name))
}

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions): FirebaseApp {
    TODO()
    // return FirebaseApp(com.google.firebase.FirebaseApp.initializeApp(context as Context, options.toAdmin()))
}

actual class FirebaseApp internal constructor(val admin: com.google.firebase.FirebaseApp) {
    actual val name: String
        get() = admin.name
    actual val options: FirebaseOptions
        get() = admin.options.run { FirebaseOptions("applicationId", "apiKey", databaseUrl, "gaTrackingId", storageBucket, projectId) }

    actual fun delete() = admin.delete()
}

actual fun Firebase.apps(context: Any?): List<FirebaseApp> {
    TODO()
    // return com.google.firebase.FirebaseApp.getApps(context as Context)
    //    .map { FirebaseApp(it) }
}

private fun FirebaseOptions.toAdmin() = com.google.firebase.FirebaseOptions.builder()
    // .setApplicationId(applicationId)
    // .setApiKey(apiKey)
    .setDatabaseUrl(databaseUrl)
    // .setGaTrackingId(gaTrackingId)
    .setStorageBucket(storageBucket)
    .setProjectId(projectId)
    // .setGcmSenderId(gcmSenderId)
    .build()