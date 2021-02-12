package com.adjarabet.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Messenger

abstract class WordHandlingMessenger {

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

    fun getClearStateMessage(): Message {
        return Message.obtain().apply {
            what = Constants.CLEAR_STATE
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