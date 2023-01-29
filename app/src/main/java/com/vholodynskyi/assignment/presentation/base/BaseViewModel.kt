package com.vholodynskyi.assignment.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vholodynskyi.assignment.util.Event
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope

open class BaseViewModel:ViewModel() {

    protected val eventChannel = Channel<Event>(BUFFERED)

    val eventFlow = eventChannel.receiveAsFlow()
}