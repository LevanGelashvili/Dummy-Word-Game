package com.adjarabet.bot

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.adjarabet.bot.utils.BotMessenger
import com.adjarabet.bot.utils.MessengerListener
import com.adjarabet.bot.utils.WordGenerator

class BotService : Service(), MessengerListener {

    private val wordGenerator  = WordGenerator()

    private var botMessenger = BotMessenger().apply {
        messengerListener = this@BotService
    }

    override fun clearState() {
        Log.d("Here", "Clearing state")
        wordGenerator.clearState()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("Here", "Unbound")
        return super.onUnbind(intent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("Here", "Binding")
        return botMessenger.binder
    }

    override fun onWordReceived(opponentsWord: String) {
        val generatedWord = wordGenerator.generateWord(opponentsWord)
        botMessenger.sendMessage(generatedWord)
    }
}