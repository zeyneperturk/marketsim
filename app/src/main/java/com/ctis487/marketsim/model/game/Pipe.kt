package com.ctis487.marketsim.model.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import kotlin.random.Random

class Pipe(
    startX: Int,
    private val pipeWidth: Int,
    private val gap: Int,
    private val speed: Int
) {
    var x: Int = startX
    var passed: Boolean = false

    private val paint = Paint().apply { color = Color.rgb(0, 180, 0) }


    private val topPipeBottom: Int = run {

        val minTop = (GameConstants.screenHeight * 0.30f).toInt()
        val maxTop = (GameConstants.screenHeight * 0.60f).toInt()
        Random.nextInt(minTop, maxTop)
    }

    fun update() {
        x -= speed
    }

    fun isOffScreen(): Boolean {
        return (x + pipeWidth < 0)
    }

    fun getTopRect(): Rect {
        val topBmp = GameConstants.bitmapBank?.pipeTop ?: return Rect()
        val topY = topPipeBottom - topBmp.height

        return Rect(
            x,
            topY,
            x + pipeWidth,
            topPipeBottom
        )
    }

    fun getBottomRect(): Rect {
        val bottomBmp = GameConstants.bitmapBank?.pipeBottom ?: return Rect()
        val bottomY = topPipeBottom + gap

        return Rect(
            x,
            bottomY,
            x + pipeWidth,
            bottomY + bottomBmp.height
        )
    }
    fun draw(canvas: Canvas) {
        val topBmp = GameConstants.bitmapBank?.pipeTop
        val bottomBmp = GameConstants.bitmapBank?.pipeBottom
        if (topBmp == null || bottomBmp == null) return

        // top pipe
        canvas.drawBitmap(
            topBmp,
            x.toFloat(),
            (topPipeBottom - topBmp.height).toFloat(),
            null
        )

        // bottom pipe
        canvas.drawBitmap(
            bottomBmp,
            x.toFloat(),
            (topPipeBottom + gap).toFloat(),
            null
        )
    }
}