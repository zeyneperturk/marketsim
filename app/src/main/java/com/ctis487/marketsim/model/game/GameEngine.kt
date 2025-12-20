package com.ctis487.marketsim.model.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class GameEngine() {
    var backgroundImage: BackgroundImage = BackgroundImage()
    var bird: Bird = Bird()


    private val pipeWidth = (GameConstants.screenWidth * 0.30f).toInt() // 0.18 -> 0.26 dene
    private val pipeGap   = (GameConstants.screenHeight * 0.35f).toInt()
    private val pipeManager = PipeManager(
        pipeWidth = pipeWidth,
        gap = pipeGap,
        speed = 10,
        distance = (GameConstants.screenWidth * 0.70f).toInt()
    )

    private var state: GameState = GameState.READY
    private var score: Int = 0

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 64f
        isAntiAlias = true
    }

    init { pipeManager.reset() }
    fun updateAndDrawBackgroundImageAndBird(canvas: Canvas) {
        val bg = GameConstants.bitmapBank?.background ?: return

        backgroundImage.backgroundImageX -= backgroundImage.backgroundImageVelocity

        val x = backgroundImage.backgroundImageX
        val y = backgroundImage.backgroundImageY

        canvas.drawBitmap(bg, x.toFloat(), y.toFloat(), null)
        canvas.drawBitmap(bg, (x + bg.width).toFloat(), y.toFloat(), null)

        // reset
        if (backgroundImage.backgroundImageX <= -bg.width) {
            backgroundImage.backgroundImageX = 0
        }

        when (state) {
            GameState.READY -> {
                bird.draw(canvas)
                canvas.drawText("Tap to Start", 80f, 120f, textPaint)
            }

            GameState.PLAYING -> {
                pipeManager.update()
                bird.update()

                pipeManager.draw(canvas)
                bird.draw(canvas)

                if (checkCollision()) {
                    state = GameState.GAME_OVER
                } else {
                    updateScore()
                }

                canvas.drawText("Score: $score", 80f, 120f, textPaint)
            }

            GameState.GAME_OVER -> {
                pipeManager.draw(canvas)
                bird.draw(canvas)
                canvas.drawText("Game Over", 80f, 120f, textPaint)
                canvas.drawText("Tap to Restart", 80f, 200f, textPaint)
                canvas.drawText("Score: $score", 80f, 280f, textPaint)
            }

            GameState.PAUSED -> {

                pipeManager.draw(canvas)
                bird.draw(canvas)

                canvas.drawText("PAUSED", 80f, 120f, textPaint)
                canvas.drawText("Tap again to resume", 80f, 200f, textPaint)
                canvas.drawText("Score: $score", 80f, 280f, textPaint)

            }
        }
    }

    fun onTouch() {
        when (state) {
            GameState.READY -> {
                state = GameState.PLAYING
                bird.flap()
            }
            GameState.PLAYING -> bird.flap()
            GameState.GAME_OVER -> restart()
            GameState.PAUSED -> state = GameState.PLAYING
            }
        }


    private fun restart() {
        score = 0
        state = GameState.READY
        pipeManager.reset()

        bird.x = GameConstants.screenWidth / 4
        bird.y = GameConstants.screenHeight / 2
    }

    private fun checkCollision(): Boolean {
        val birdRect = bird.getRect()
        for (p in pipeManager.getPipes()) {
            if (Rect.intersects(birdRect, p.getTopRect()) || Rect.intersects(birdRect, p.getBottomRect())) {
                return true
            }
        }
        return false
    }

    private fun updateScore() {
        val birdX = bird.x
        for (p in pipeManager.getPipes()) {

            if (!p.passed && birdX > p.x + (GameConstants.screenWidth * 0.18f).toInt()) {
                p.passed = true
                score++
            }
        }
    }

    fun togglePause() {
        state = when (state) {
            GameState.PLAYING -> GameState.PAUSED
            GameState.PAUSED -> GameState.PLAYING
            else -> state
        }
    }
}
