package com.ctis487.marketsim.model.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.media3.exoplayer.ExoPlayer
import com.ctis487.marketsim.R



class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private var gameThread: GameThread? = null
    private var tapPlayer: ExoPlayer? = null
    fun setTapPlayer(player: ExoPlayer) {
        tapPlayer = player
    }

    init {
        initView()
    }
    override fun surfaceCreated(holder: SurfaceHolder) {
        gameThread = GameThread(holder)
        gameThread?.isRunning = true
        gameThread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

        var retry = true
        gameThread?.isRunning = false

        while (retry) {
            try {

                gameThread?.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        gameThread = null
    }

    private fun initView() {
        // SurfaceHolder callback'lerini dinlemeye başla
        holder.addCallback(this)
        isFocusable = true

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {

            tapPlayer?.apply {
                seekTo(0)          // başa sar
                playWhenReady = true
                play()
            }

            GameConstants.gameEngine?.onTouch()
            return true
        }
        return super.onTouchEvent(event)
    }
}
