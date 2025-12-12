package com.ctis487.marketsim

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ctis487.marketsim.databinding.ActivityMainBinding
import com.ctis487.marketsim.databinding.ActivityProfileUpdateBinding

class ProfileUpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var binding: ActivityProfileUpdateBinding

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}