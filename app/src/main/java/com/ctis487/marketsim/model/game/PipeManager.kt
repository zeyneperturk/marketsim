package com.ctis487.marketsim.model.game

import android.graphics.Canvas

class PipeManager(
    private val pipeWidth: Int,
    private val gap: Int,
    private val speed: Int,
    private val distance: Int
) {
    private val pipes = mutableListOf<Pipe>()

    fun reset() {
        pipes.clear()
        pipes.add(Pipe(GameConstants.screenWidth + 200, pipeWidth, gap, speed))
    }

    fun update() {
        if (pipes.isEmpty()) reset()

        // yeni payp ekleme
        val last = pipes.last()
        if (last.x < GameConstants.screenWidth - distance) {
            pipes.add(Pipe(GameConstants.screenWidth + 200, pipeWidth, gap, speed))
        }

        pipes.forEach { it.update() }

        // soldan çıkanları sil
        while (pipes.isNotEmpty() && pipes.first().isOffScreen()) {
            pipes.removeAt(0)
        }
    }

    fun draw(canvas: Canvas) {
        pipes.forEach { it.draw(canvas) }
    }

    fun getPipes(): List<Pipe> {
        return pipes
    }
}