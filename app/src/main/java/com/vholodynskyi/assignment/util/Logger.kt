package com.vholodynskyi.assignment.util

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.java.KoinJavaComponent.inject

object Logger {
    private val auth by inject<FirebaseAuth>(FirebaseAuth::class.java)
    private val analytics by inject<FirebaseAnalytics>(FirebaseAnalytics::class.java)
    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    fun fba(name: String, block: ParametersBuilder.() -> Unit) {
        analytics.setUserId(auth.currentUser?.uid)
        analytics.logEvent(name) { block }
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
        throwable?.let {
            FirebaseCrashlytics.getInstance().recordException(it)
        }
    }
}