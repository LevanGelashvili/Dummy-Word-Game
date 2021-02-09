package com.adjarabet.user

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adjarabet.user.databinding.ActivityUserBinding
import kotlin.random.Random


class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    var myService: Messenger? = null
    var isBound = false

    private val userMessenger by lazy { Messenger(UserHandler(this)) }

    inner class UserHandler(
            private val context: Context,
            private val applicationContext: Context = context.applicationContext
    ) : Handler() {

        override fun handleMessage(msg: Message) {
            Toast.makeText(context, "RECEIEVED FROM SERVER ${msg.data.getInt("Here")}", Toast.LENGTH_LONG).show()
        }
    }

    private val myConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
                className: ComponentName,
                service: IBinder) {
            myService = Messenger(service)
            isBound = true
        }

        override fun onServiceDisconnected(
                className: ComponentName) {
            myService = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBot.setOnClickListener {
            launchBotActivity()
        }

        binding.bindToService.setOnClickListener {
            bindToService()
        }

        binding.sendMessage.setOnClickListener {
            sendMessage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopBotService()
    }

    private fun sendMessage() {
        if (!isBound) return

        val msg: Message = Message.obtain()

        val bundle = Bundle()
        val i = Random.nextInt(0, 10)
        Toast.makeText(this, "Sending $i", Toast.LENGTH_SHORT).show()
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
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
    }

    private fun launchBotActivity() {
        val launchIntent = packageManager.getLaunchIntentForPackage(BOT_PACKAGE_NAME)
        startActivity(launchIntent)
    }

    private fun stopBotService() {
        val intent = Intent()
        intent.component = ComponentName(BOT_PACKAGE_NAME, BOT_SERVICE_NAME)
        stopService(intent)
    }

    //TODO: UNBIND

    companion object {
        const val BOT_PACKAGE_NAME = "com.adjarabet.bot"
        const val BOT_SERVICE_NAME = "com.adjarabet.bot.RemoteService"
    }
}