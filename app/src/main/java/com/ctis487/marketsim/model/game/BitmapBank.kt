package com.ctis487.marketsim.model.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ctis487.marketsim.R

class BitmapBank (res: Resources){

    var background: Bitmap = BitmapFactory.decodeResource(res, R.drawable.background)
    var bird: Bitmap = BitmapFactory.decodeResource(res,R.drawable.jt)
    var pipeTop: Bitmap = BitmapFactory.decodeResource(res,R.drawable.pipetop)
    var pipeBottom: Bitmap = BitmapFactory.decodeResource(res,R.drawable.pipebottom)


    init {
        background = scaleImage(background)
        bird = scaleBird(bird)

        pipeTop = scalePipe(pipeTop)
        pipeBottom = scalePipe(pipeBottom)
    }
    fun returnBackgroundWidth(): Int {
        return background.width
    }

    fun returnBackgroundHeight(): Int {
        return background.height
    }

    fun returnBirdWidth(): Int{
        return bird.width
    }

    fun returnBirdHeight(): Int{
        return bird.height
    }

    fun scaleImage(bitmap: Bitmap): Bitmap{

        val widthHeightRatio = background.width.toFloat() / background.height.toFloat()
        var backgroundScaledWidth: Int = (widthHeightRatio * GameConstants.screenWidth).toInt()

        return (Bitmap.createScaledBitmap(bitmap,backgroundScaledWidth, GameConstants.screenHeight, false))
    }
    private fun scaleBird(bitmap: Bitmap): Bitmap {

        val targetH = (GameConstants.screenHeight * 0.10f).toInt()
        val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val targetW = (targetH * ratio).toInt()

        return (Bitmap.createScaledBitmap(bitmap, targetW, targetH, false))
    }

    private fun scalePipe(bitmap: Bitmap): Bitmap {

        val targetHeight = (GameConstants.screenHeight * 0.65f).toInt()
        val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val targetWidth = (targetHeight * ratio).toInt()

        return (Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false))
    }
}