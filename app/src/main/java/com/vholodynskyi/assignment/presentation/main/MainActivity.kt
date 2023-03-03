package com.vholodynskyi.assignment.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.firebase.ui.auth.AuthUI
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.vholodynskyi.assignment.presentation.details.BackButton
import com.vholodynskyi.assignment.presentation.navigation.NavigationComponent
import com.vholodynskyi.assignment.util.Logger
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseCrashlytics: FirebaseCrashlytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        firebaseCrashlytics = Firebase.crashlytics

        Logger.fba(
            FirebaseAnalytics.Event.SCREEN_VIEW
        ) { param(FirebaseAnalytics.Param.SCREEN_NAME, "Main") }


        setContent {
            NavigationComponent()
        }
    }
}