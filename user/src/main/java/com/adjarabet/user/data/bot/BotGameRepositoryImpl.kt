package com.adjarabet.user.data.bot

import android.content.pm.PackageManager
import android.util.Log
import com.adjarabet.user.app.App
import com.adjarabet.user.domain.repository.GameRepository
import com.adjarabet.user.utils.Result

class BotGameRepositoryImpl : GameRepository {

    private var userMessenger = UserMessenger()

    override fun getOpponentsWord(
        myWord: String,
        onOpponentWordReceived: (Result<String>) -> Unit
    ) {
        userMessenger.onOpponentWordReceived = onOpponentWordReceived
        userMessenger.sendMessage(myWord)
    }

    override fun initOpponent(onOpponentInitialized: (Result<Unit>) -> Unit) {
        try {
            tryToConnectWithBot(onOpponentInitialized)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            onOpponentInitialized(Result.Error(e))
        }
    }

    override fun shutdownOpponent() {
        userMessenger.shutdownOpponent()
    }

    private fun tryToConnectWithBot(
        onOpponentInitialized: (Result<Unit>) -> Unit
    ) {
        val packageManager = App.context.packageManager
        packageManager.getPackageInfo(BOT_PACKAGE_NAME, BOT_PACKAGE_FLAG)

        val launchIntent = packageManager.getLaunchIntentForPackage(BOT_PACKAGE_NAME)
        launchIntent?.let {
            App.context.startActivity(it)
        }

        userMessenger.onSuccessfullyInitialized = onOpponentInitialized
        userMessenger.bindToService()
    }

    companion object {
        const val BOT_PACKAGE_NAME = "com.adjarabet.bot"
        const val BOT_SERVICE_NAME = "com.adjarabet.bot.BotService"
        const val BOT_PACKAGE_FLAG = 0
    }
}