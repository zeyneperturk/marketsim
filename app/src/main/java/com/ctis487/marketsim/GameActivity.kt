package com.ctis487.marketsim

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.ctis487.marketsim.model.game.GameView

class GameActivity : AppCompatActivity() {

    private lateinit var bgPlayer: ExoPlayer
    private lateinit var tapPlayer: ExoPlayer

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_game)

        val gameView = findViewById<GameView>(R.id.gameView)
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish() // MainActivity’ye geri
        }

        // BACKGROUND MUSIC
        bgPlayer = ExoPlayer.Builder(this)
            .setHandleAudioBecomingNoisy(false)
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

        // GameView'e tapPlayer veriyoruz
        gameView.setTapPlayer(tapPlayer)
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
}