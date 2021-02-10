package com.adjarabet.user.data.bot

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import com.adjarabet.common.messenger.RpcMessenger
import com.adjarabet.user.app.App
import com.adjarabet.user.utils.Result

class UserMessenger : RpcMessenger() {

    private var isBound: Boolean = false
    private var serviceMessenger: Messenger? = null

    var onOpponentWordReceived: ((Result<String>) -> Unit)? = null
    var onSuccessfullyInitialized: ((Result<Unit>) -> Unit)? = null

    private val myConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            serviceMessenger = Messenger(service)
            isBound = true
            onSuccessfullyInitialized?.invoke(Result.Success(Unit))
        }

        override fun onServiceDisconnected(
            className: ComponentName
        ) {
            serviceMessenger = null
            isBound = false
        }
    }

    override fun handleIncomingMessage(msg: Message) {
        val word = getWordFromMessage(msg)
        onOpponentWordReceived?.invoke(Result.Success(word))
    }

    override fun sendMessage(word: String) {
        if (isBound) {
            val msg = getMessageFromWord(word).apply {
                replyTo = messenger
            }

            try {
                serviceMessenger!!.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    fun launchOpponentApp() {
        val launchIntent = App.context.packageManager.getLaunchIntentForPackage(BOT_PACKAGE_NAME)
        App.context.startActivity(launchIntent)
    }

    fun bindToService() {
        val intent = Intent()
        intent.component = ComponentName(BOT_PACKAGE_NAME, BOT_SERVICE_NAME)
        App.context.bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
    }

    companion object {
        const val BOT_PACKAGE_NAME = "com.adjarabet.bot"
        const val BOT_SERVICE_NAME = "com.adjarabet.bot.RemoteService"
    }
}