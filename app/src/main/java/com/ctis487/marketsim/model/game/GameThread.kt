package com.ctis487.marketsim.model.game

import android.graphics.Canvas
import android.os.SystemClock
import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder) : Thread() {

     var isRunning: Boolean = true

     var startTime: Long = 0
     var loopTime: Long = 0
     val delay: Long = 33 // ~30 FPS

    override fun run(){
        while(isRunning){
            startTime = SystemClock.uptimeMillis()

            var canvas : Canvas? = surfaceHolder.lockCanvas(null)
            if(canvas != null){
                synchronized(surfaceHolder){
                    GameConstants.gameEngine?.updateAndDrawBackgroundImageAndBird(canvas)

                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }

            loopTime = SystemClock.uptimeMillis() - startTime
            // pause
            if(loopTime < delay) {
                try {
                    sleep(delay - loopTime)
                } catch (e: InterruptedException){

                }
            }
        }
    }

}