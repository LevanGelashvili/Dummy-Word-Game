package com.adjarabet.user.data

import android.content.Context
import android.content.pm.PackageManager
import com.adjarabet.user.domain.repository.GameRepository
import com.adjarabet.user.utils.Result
import javax.inject.Inject

class BotGameRepositoryImpl @Inject constructor(
    private val context: Context
) : GameRepository {

    private var userMessenger = UserMessenger(context)

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

    override fun clearOpponentState(onOpponentStateCleared: (Result<Unit>) -> Unit) {
        userMessenger.onOpponentStateCleared = onOpponentStateCleared
        userMessenger.clearOpponentState()
    }

    private fun tryToConnectWithBot(
        onOpponentInitialized: (Result<Unit>) -> Unit
    ) {
        val packageManager = context.packageManager
        packageManager.getPackageInfo(BOT_PACKAGE_NAME, BOT_PACKAGE_FLAG)

        userMessenger.onSuccessfullyInitialized = onOpponentInitialized
        userMessenger.bindToService()
    }

    companion object {
        const val BOT_PACKAGE_NAME = "com.adjarabet.bot"
        const val BOT_SERVICE_NAME = "com.adjarabet.bot.BotService"
        const val BOT_PACKAGE_FLAG = 0
    }
}