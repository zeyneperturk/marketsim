package com.ctis487.marketsim.model.game

import android.content.Context

class GameConstants {

    companion object {
        var bitmapBank : BitmapBank? = null
        var gameEngine: GameEngine? = null

        var screenWidth = 0
        var screenHeight = 0


        fun initalization(context: Context){

            setScreenSize(context)
            bitmapBank = BitmapBank(context.resources)
            gameEngine = GameEngine()
        }

         fun setScreenSize(context: Context) {
            val metrics = context.resources.displayMetrics
            screenWidth = metrics.widthPixels
            screenHeight = metrics.heightPixels
        }
    }
}