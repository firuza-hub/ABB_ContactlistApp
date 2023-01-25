package com.vholodynskyi.assignment.util

sealed  class Event( val message:String? = null) {
    class ShowToaster(message:String):Event(message)
}