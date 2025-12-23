package com.ctis487.marketsim

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ctis487.lab.myapplication.LocaleHelper
import com.ctis487.marketsim.model.game.GameConstants
import com.ctis487.marketsim.model.game.GameView
import com.ctis487.marketsim.worker.CouponWorker

class GameActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase))
        )
    }

    private lateinit var bgPlayer: ExoPlayer
    private lateinit var tapPlayer: ExoPlayer

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_game)
        GameConstants.initalization(this) // applicationContext değil, this daha iyi

        val gameView = findViewById<GameView>(R.id.gameView)
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish() // MainActivity
        }

        // BACKGROUND MUSIC
        bgPlayer = ExoPlayer.Builder(this)
            .setHandleAudioBecomingNoisy(true)
            .build()

        val bgItem = MediaItem.fromUri(
            RawResourceDataSource.buildRawResourceUri(R.raw.bgmusic)
        )
        bgPlayer.setMediaItem(bgItem)
        bgPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
        bgPlayer.prepare()
        bgPlayer.play()

        // TAP SOUND
        tapPlayer = ExoPlayer.Builder(this).build()
        val tapItem = MediaItem.fromUri(
            RawResourceDataSource.buildRawResourceUri(R.raw.tapsound)
        )
        tapPlayer.setMediaItem(tapItem)
        tapPlayer.prepare()

        gameView.setTapPlayer(tapPlayer)

        //Generating coupons
        GameConstants.gameEngine?.onGameOver = { score ->
            startCouponWorker(score)
            if (score > 10) {
                runOnUiThread {
                    showCouponDialog()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        bgPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        bgPlayer.release()
        tapPlayer.release()
    }

    override fun onResume() {
        super.onResume()
        bgPlayer.playWhenReady = true
        bgPlayer.play()
    }

    //coupon related
    private fun startCouponWorker(score: Int) {
        val data = workDataOf("score" to score)

        val request = OneTimeWorkRequestBuilder<CouponWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "coupon_worker_once",
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    private fun showCouponDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_coupon, null)

        val btnOk = dialogView.findViewById<Button>(R.id.btnOk)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}