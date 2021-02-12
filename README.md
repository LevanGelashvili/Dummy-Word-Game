# Dummy word game

## Game Description
Dummy word game is a project consisting of 2 applications: Bot and User. Player and bot make moves 1 by 1, each repeating already said words and adding one of their own. Whoever is not able to repeat all words (or makes a different kind of a mistake) loses the game.

## Installation

Clone this repository and open it in Android Studio.

## Bot
Firstly, bot should be installed. Error message will be displayed if user is launched without having bot installed. Since bot is a background service and has no UI, it can only be installed this way:
> Bot -> Edit Configurations -> Launch Options -> Launch -> Nothing

Bot can not be launched manually, since user application has to start it

## User
User app allows player to communicate with bot, sending words, validating them and displaying endgame conditions at appropriate times. Bot is shut down automatically when user activity is completely destroyed.

## Additional Notes
Used bound service and messengers for RPCs.
MVVM + Clean Arch + Dagger2 for User app.