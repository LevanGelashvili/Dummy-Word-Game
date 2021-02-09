package com.adjarabet.user.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.widget.Toast
import com.adjarabet.user.app.App
import com.adjarabet.user.domain.entities.Word
import com.adjarabet.user.domain.repository.GameRepository
import io.reactivex.Completable
import io.reactivex.Single
import kotlin.random.Random

class BotGameRepositoryImpl : GameRepository {

    override fun initOpponent(): Completable {
        return Completable.complete()
    }

    override fun getOpponentsWord(myWord: Word): Single<String> {
        return Single.just("")
    }

    var myService: Messenger? = null
    var isBound = false

    private val userMessenger by lazy { Messenger(UserHandler(App.context)) }

    inner class UserHandler(
        private val context: Context
    ) : Handler() {

        override fun handleMessage(msg: android.os.Message) {
            Toast.makeText(
                context,
                "RECEIEVED FROM SERVER ${msg.data.getInt("Here")}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val myConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            myService = Messenger(service)
            isBound = true
        }

        override fun onServiceDisconnected(
            className: ComponentName
        ) {
            myService = null
            isBound = false
        }
    }

    private fun sendMessage() {
        if (!isBound) return

        val msg: Message = Message.obtain()

        val bundle = Bundle()
        val i = Random.nextInt(0, 10)
        Toast.makeText(App.context, "Sending $i", Toast.LENGTH_SHORT).show()
        bundle.putInt("Here", i)

        msg.data = bundle
        msg.replyTo = userMessenger

        try {
            myService!!.send(msg)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun bindToService() {
        val intent = Intent()
        intent.component = ComponentName(BOT_PACKAGE_NAME, BOT_SERVICE_NAME)
        App.context.bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
    }

    private fun launchBotActivity() {
        val launchIntent = App.context.packageManager.getLaunchIntentForPackage(BOT_PACKAGE_NAME)
        App.context.startActivity(launchIntent)
    }

    private fun stopBotService() {
        val intent = Intent()
        intent.component = ComponentName(BOT_PACKAGE_NAME, BOT_SERVICE_NAME)
        App.context.stopService(intent)
    }

    companion object {
        const val BOT_PACKAGE_NAME = "com.adjarabet.bot"
        const val BOT_SERVICE_NAME = "com.adjarabet.bot.RemoteService"
    }
}