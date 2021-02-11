package com.adjarabet.bot.utils

import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import com.adjarabet.common.Constants
import com.adjarabet.common.WordHandlingMessenger

class BotMessenger : WordHandlingMessenger() {

    val binder: IBinder?
        get() = messenger.binder

    private var clientMessenger: Messenger? = null
    var messengerListener: MessengerListener? = null

    override fun handleIncomingMessage(msg: Message) {

        val shutdownBot = msg.what == Constants.BOT_SHUT_DOWN

        if (shutdownBot) {
            messengerListener?.shutdownService()
        } else {
            val receivedWord = getWordFromMessage(msg)
            clientMessenger = msg.replyTo
            messengerListener?.onWordReceived(receivedWord)
        }
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