package com.adjarabet.bot

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import com.adjarabet.common.messenger.RpcMessenger

// TODO: Maybe add an interface between these two for callbacks

class RemoteService : Service() {

    private var botMessenger = BotMessenger()

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("Here", "BIND SUCCESSFUL")
        return botMessenger.binder
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