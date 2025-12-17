package com.ctis487.marketsim.model

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    init {
        initView()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // şimdilik boş kalsın, crash olmasın
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // şimdilik boş
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // şimdilik boş
    }

    private fun initView() {
        holder.addCallback(this)
        isFocusable = true
    }
}