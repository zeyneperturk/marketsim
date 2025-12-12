package com.ctis487.marketsim

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ctis487.marketsim.custom.UserProfileView
import com.ctis487.marketsim.databinding.ActivityMainBinding
import com.ctis487.marketsim.databinding.ActivityProfileBinding
import com.ctis487.marketsim.model.User

class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var user = prepUserForTesting()
        binding.userProfileView.setUser(user)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.userProfileView.setOnProfileEditListener(object: UserProfileView.OnProfileEditListener{
            override fun onEditClicked(view: View) {
                val intent = Intent(this@ProfileActivity, ProfileUpdateActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun prepUserForTesting(): User {
        return User(
            uid = 1,
            username = "jdoe",
            firstName = "John",
            lastName = "Doe",
            iconUrl = ""
        )
    }
}