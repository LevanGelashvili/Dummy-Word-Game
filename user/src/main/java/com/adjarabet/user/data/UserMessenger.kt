package com.adjarabet.user.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import com.adjarabet.common.Constants
import com.adjarabet.common.WordHandlingMessenger
import com.adjarabet.user.data.BotGameRepositoryImpl.Companion.BOT_PACKAGE_NAME
import com.adjarabet.user.data.BotGameRepositoryImpl.Companion.BOT_SERVICE_NAME
import com.adjarabet.user.utils.Result

class UserMessenger(private val context: Context) : WordHandlingMessenger() {

    private var isBound: Boolean = false
    private var serviceMessenger: Messenger? = null

    var onOpponentWordReceived: ((Result<String>) -> Unit)? = null
    var onSuccessfullyInitialized: ((Result<Unit>) -> Unit)? = null
    var onOpponentStateCleared: ((Result<Unit>) -> Unit)? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
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

        val opponentStateClearedSuccessfully = msg.what == Constants.CLEAR_STATE

        if (opponentStateClearedSuccessfully) {
            onOpponentStateCleared?.invoke(Result.Success(Unit))
        } else {
            val word = getWordFromMessage(msg)
            onOpponentWordReceived?.invoke(Result.Success(word))
        }
    }

    override fun sendMessage(word: String) {
        if (isBound) {
            val msg = getMessageFromWord(word).apply {
                replyTo = messenger
            }

            try {
                serviceMessenger?.send(msg)
            } catch (e: RemoteException) {
                onOpponentWordReceived?.invoke(Result.Error(e))
                e.printStackTrace()
            }
        }
    }

    fun clearOpponentState() {
        if (isBound) {
            try {
                serviceMessenger?.send(getClearStateMessage())
            } catch (e: RemoteException) {
                onOpponentStateCleared?.invoke(Result.Error(e))
                e.printStackTrace()
            }
        }
    }

    fun bindToService() {
        val intent = Intent()
        intent.component = ComponentName(BOT_PACKAGE_NAME, BOT_SERVICE_NAME)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}