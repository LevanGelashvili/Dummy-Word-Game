package com.adjarabet.bot.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adjarabet.bot.BotService
import com.adjarabet.bot.R

class BotActivity : AppCompatActivity() {

    private val serviceIntent by lazy {
        Intent(this, BotService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bot)

        startService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(serviceIntent)
    }
}