package com.adjarabet.bot

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast

class RemoteService : Service() {

    private lateinit var messenger: Messenger

    inner class IncomingHandler(
            private val context: Context,
            private val applicationContext: Context = context.applicationContext
    ) : Handler() {

        override fun handleMessage(msg: Message) {
            Log.d("Here", "Handling message!")

            val msgInt = msg.data.getInt("Here")
            Toast.makeText(context, "Received $msgInt", Toast.LENGTH_LONG).show()

            try {
                msg.replyTo.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("Here", "BIND SUCCESSFUL")
        messenger = Messenger(IncomingHandler(this))
        return messenger.binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("Here", "Service started")
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        Log.d("Here", "Service Destroyed")
    }
}