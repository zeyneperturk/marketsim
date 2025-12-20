package com.ctis487.marketsim.model.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Bird {

    var x: Int = GameConstants.screenWidth / 4
    var y: Int = GameConstants.screenHeight / 2

    private var velocityY: Float = 0f // speed

    private val gravity: Float = 1.2f     // fall
    private val flapPower: Float = -18f   // jump

    fun update(){

        velocityY += gravity
        y = (y + velocityY).toInt()

        // limits
        if (y < 0) { y = 0; velocityY = 0f }
        if (y > GameConstants.screenHeight) { y = GameConstants.screenHeight; velocityY = 0f }

    }

    fun flap() {
        velocityY = flapPower
    }

    fun draw(canvas: Canvas) {
        val bmp = GameConstants.bitmapBank?.bird
        if (bmp != null) {
            canvas.drawBitmap(
                bmp,
                (x - bmp.width / 2).toFloat(),
                (y - bmp.height / 2).toFloat(),
                null
            )
        }
    }

    fun getRect(): Rect {
        val bmp = GameConstants.bitmapBank?.bird
        val w = bmp?.width ?: 80
        val h = bmp?.height ?: 80

        val left = x - w / 2
        val top  = y - h / 2
        val right = x + w / 2
        val bottom = y + h / 2

        val r = Rect(left, top, right, bottom)


        val insetX = (w * 0.18f).toInt()
        val insetY = (h * 0.18f).toInt()
        r.inset(insetX, insetY)

        return r
    }
}