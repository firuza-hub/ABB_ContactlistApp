package com.vholodynskyi.assignment.presentation.main

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.vholodynskyi.assignment.presentation.base.BaseViewModel
import com.vholodynskyi.assignment.util.Event
import com.vholodynskyi.assignment.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val auth: FirebaseAuth
) : BaseViewModel() {

    private val _isAuthorized = MutableStateFlow(true)
    val isAuthorized = _isAuthorized.asStateFlow()



    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user.asStateFlow()

    init {
        _user.value = auth.currentUser
        _isAuthorized.value = auth.currentUser != null
    }

    fun crashTheApp(){
        throw Exception("intentional app crash")
    }

    fun signOut(context:Context) {
        Log.i("FLOW_TEST", "signOut triggered")
        viewModelScope.launch(Dispatchers.IO) {
            AuthUI.getInstance()
                .signOut(context)
            _user.value = null
            _isAuthorized.value = false

            Log.i("FLOW_TEST", "isAuth set to ${isAuthorized.value}")
        }
    }

    fun buildLoginIntent(): Intent {
        Log.i("FLOW_TEST", "buildLoginIntent triggered")
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        viewModelScope.launch(Dispatchers.Default) {
            Log.i("FLOW_TEST", "onSignInResult triggered")
            val response = result.idpResponse

            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                _user.value = auth.currentUser
                _isAuthorized.value = true
                auth.currentUser?.let {
                    eventChannel.send(Event.ShowToaster("Welcome ${it.email}"))
                    Logger.fba(FirebaseAnalytics.Event.LOGIN) { param(FirebaseAnalytics.Param.ITEM_NAME, "${it.email}") }
                }
            } else {
                if (response == null) {
                    eventChannel.send(Event.ShowToaster("Sign in cancelled"))
                } else {
                    eventChannel.send(Event.ShowToaster("Sign in failed ${response.error?.let { "($it.message)" }}"))
                }
            }
        }
    }
}