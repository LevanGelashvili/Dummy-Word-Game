package com.adjarabet.user.data.bot

import com.adjarabet.user.app.App
import com.adjarabet.user.domain.repository.GameRepository
import com.adjarabet.user.utils.Result

class BotGameRepositoryImpl : GameRepository {

    private var userMessenger = UserMessenger()

    override fun initOpponent(onSuccessfullyInitialized: (Result<Unit>) -> Unit) {
        val launchIntent = App.context.packageManager.getLaunchIntentForPackage(UserMessenger.BOT_PACKAGE_NAME)
        App.context.startActivity(launchIntent)

        userMessenger.onSuccessfullyInitialized = onSuccessfullyInitialized
        userMessenger.bindToService()
    }

    override fun getOpponentsWord(
        myWord: String,
        onOpponentWordReceived: (Result<String>) -> Unit
    ) {
        userMessenger.onOpponentWordReceived = onOpponentWordReceived
        userMessenger.sendMessage(myWord)
    }
}