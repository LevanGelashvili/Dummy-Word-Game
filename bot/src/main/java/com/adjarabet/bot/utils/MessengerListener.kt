package com.adjarabet.bot.utils

interface MessengerListener {
    fun onWordReceived(opponentsWord: String)
    fun shutdownService()
}