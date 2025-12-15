package com.ctis487.marketsim

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ctis487.lab.myapplication.LocaleHelper
import com.ctis487.marketsim.custom.UserProfileView
import com.ctis487.marketsim.databinding.ActivityMainBinding
import com.ctis487.marketsim.databinding.ActivityProfileBinding
import com.ctis487.marketsim.model.User

class ProfileActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    lateinit var binding: ActivityProfileBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = prepUserForTesting()
        binding.userProfileView.setUser(user)

        val spinnerItems = resources.getStringArray(R.array.langSpinner)
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerItems)
        binding.spinnerLang.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.userProfileView.setOnProfileEditListener(object: UserProfileView.OnProfileEditListener{
            override fun onEditClicked(view: View) {
                val intent = Intent(this@ProfileActivity, ProfileUpdateActivity::class.java)
                intent.putExtra("user", user)
                resultLauncher.launch(intent)
            }
        })

        val currentLanguage = LocaleHelper.getLanguage(this)
        val currentIndex = when (currentLanguage) {
            "en" -> 0
            "tr" -> 1
            else -> 0
        }
        binding.spinnerLang.setSelection(currentIndex)

        binding.spinnerLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var firstCall = true

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (firstCall) {
                    firstCall = false
                    return
                }

                val selectedLanguage = when (position) {
                    0 -> "en"
                    1 -> "tr"
                    else -> "en"
                }

                val prev = LocaleHelper.getLanguage(this@ProfileActivity)
                if (selectedLanguage == prev) return

                val newContext = LocaleHelper.setLocale(this@ProfileActivity, selectedLanguage)
                //Toast.makeText(newContext, newContext.getString(R.string.toastLangChange), Toast.LENGTH_SHORT).show()

                recreate()
            }

            override fun onNothingSelected(parent: AdapterView<*>) { }
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if(result.resultCode == RESULT_OK){
            val res = result.data
            val updatedUser = res?.getParcelableExtra<User>("user")!!
            user = updatedUser
            binding.userProfileView.setUser(user)
        }
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