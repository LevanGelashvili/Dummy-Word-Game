package com.adjarabet.bot

import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import com.adjarabet.common.messenger.RpcMessenger

class BotMessenger : RpcMessenger() {

    var binder: IBinder? = null
        get() = messenger.binder

    private var clientMessenger: Messenger? = null

    override fun handleIncomingMessage(msg: Message) {
        val receivedWord = getWordFromMessage(msg)
        clientMessenger = msg.replyTo
        // generate new word
        // add it to set
        // basic game logic
        // and in another function send message like below
        sendMessage("$receivedWord Duh")
    }

    override fun sendMessage(word: String) {
        val newMessage = getMessageFromWord(word)
        try {
            clientMessenger?.send(newMessage)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }
}