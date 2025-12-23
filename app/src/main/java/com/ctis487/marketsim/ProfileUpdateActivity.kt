package com.ctis487.marketsim

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ctis487.marketsim.databinding.ActivityMainBinding
import com.ctis487.marketsim.databinding.ActivityProfileUpdateBinding
import com.ctis487.marketsim.model.User
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.ctis487.lab.myapplication.LocaleHelper
import com.ctis487.marketsim.db.MarketRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ProfileUpdateActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    lateinit var binding: ActivityProfileUpdateBinding
    private var currentUser: User? = null
    private var newImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = MarketRoomDatabase.getDatabase(applicationContext)
        val userDao = db.UserDAO()
        currentUser = userDao.getById(1)
        binding.user = currentUser

        if (!currentUser?.iconUrl.isNullOrEmpty()) {
            binding.imgProfileUpdate.setImageURI(currentUser!!.iconUrl.toUri())
        }

        binding.imgCamera.setOnClickListener { launchPhotoPicker() }
        binding.imgProfileUpdate.setOnClickListener { launchPhotoPicker() }

        binding.btnUpdate.setOnClickListener {
            if (validateInputs()) {
                sendBackResult()
            }
        }

        binding.btnCancel.setOnClickListener { finish() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun launchPhotoPicker() {

        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun validateInputs(): Boolean {

        val nameRegex = Regex("^[a-zA-ZğüşöçıİĞÜŞÖÇ ]+$")

        if (binding.etUsername.text.isNullOrBlank()) {
            binding.tilUsername.error = "Required"
            return false
        }

        val firstName = binding.etFirstName.text.toString()
        if (firstName.isBlank()) {
            binding.tilFirstName.error = "Required"
            return false
        }
        if (!nameRegex.matches(firstName)) {
            binding.tilFirstName.error = "Only letters allowed"
            return false
        }

        val lastName = binding.etLastName.text.toString()
        if (lastName.isBlank()) {
            binding.tilLastName.error = "Required"
            return false
        }
        if (!nameRegex.matches(lastName)) {
            binding.tilLastName.error = "Only letters allowed"
            return false
        }

        return true
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            newImageUri = uri

            binding.imgProfileUpdate.setImageURI(uri)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private fun sendBackResult() {

        lifecycleScope.launch(Dispatchers.IO) {
            var finalIconUrl = currentUser?.iconUrl ?: ""

            if (newImageUri != null) {
                finalIconUrl = saveImageToInternalStorage(newImageUri!!)
            }

            val updatedUser = User(
                uid = currentUser!!.uid,
                username = binding.etUsername.text.toString(),
                firstName = binding.etFirstName.text.toString(),
                lastName = binding.etLastName.text.toString(),
                iconUrl = finalIconUrl
            )

            withContext(Dispatchers.Main) {
                val resultIntent = Intent()
                resultIntent.putExtra("user", updatedUser)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val file = File(filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            Uri.fromFile(file).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}