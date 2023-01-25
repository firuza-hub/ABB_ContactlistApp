package com.vholodynskyi.assignment.presentation.base

import androidx.lifecycle.ViewModel
import com.vholodynskyi.assignment.util.Event
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow

open class BaseViewModel:ViewModel() {
    protected val eventChannel = Channel<Event>(BUFFERED)

    val eventFlow = eventChannel.receiveAsFlow()
}