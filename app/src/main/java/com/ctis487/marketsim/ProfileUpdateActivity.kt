package com.ctis487.marketsim

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ProfileUpdateActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileUpdateBinding
    private var currentUser: User? = null
    private var newImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = intent.getParcelableExtra<User>("user")
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
        // Launches the modern Android Photo Picker
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun validateInputs(): Boolean {
        // (Same validation logic as before)
        if (binding.etUsername.text.isNullOrBlank()) {
            binding.tilUsername.error = "Required"
            return false
        }
        return true
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            newImageUri = uri
            // Show the selected image immediately in the UI
            binding.imgProfileUpdate.setImageURI(uri)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private fun sendBackResult() {
        // Use coroutine just for file I/O (saving image)
        lifecycleScope.launch(Dispatchers.IO) {
            var finalIconUrl = currentUser?.iconUrl ?: ""

            // Save image locally if a new one was picked
            if (newImageUri != null) {
                finalIconUrl = saveImageToInternalStorage(newImageUri!!)
            }

            // Create updated User object
            val updatedUser = User(
                uid = currentUser!!.uid,
                username = binding.etUsername.text.toString(),
                firstName = binding.etFirstName.text.toString(),
                lastName = binding.etLastName.text.toString(),
                iconUrl = finalIconUrl
            )

            // Switch back to Main Thread to finish activity
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