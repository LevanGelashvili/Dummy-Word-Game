package com.adjarabet.bot.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.adjarabet.bot.BotService

class BotActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serviceIntent = Intent(this, BotService::class.java)
        startService(serviceIntent)
        finish()
    }
}