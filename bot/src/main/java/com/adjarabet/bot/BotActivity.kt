package com.adjarabet.bot

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class BotActivity : AppCompatActivity() {

    private val serviceIntent by lazy {
        Intent(this, RemoteService::class.java)
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