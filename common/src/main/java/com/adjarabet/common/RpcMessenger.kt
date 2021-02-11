package com.adjarabet.common

import android.annotation.SuppressLint
import android.os.*

abstract class RpcMessenger {

    private var incomingHandler = IncomingHandler()
    protected val messenger: Messenger by lazy {
        Messenger(incomingHandler)
    }

    abstract fun handleIncomingMessage(msg: Message)

    abstract fun sendMessage(word: String)

    fun getMessageFromWord(word: String): Message {
        return Message.obtain().apply {
            data = Bundle().apply {
                putString(MESSAGE_KEY, word)
            }
        }
    }

    fun getWordFromMessage(msg: Message): String {
        return msg.data.getString(MESSAGE_KEY)!!
    }

    @SuppressLint("HandlerLeak")
    inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            handleIncomingMessage(msg)
        }
    }

    companion object {
        const val MESSAGE_KEY = "msg_key"
    }
}