package com.setianjay.mydigitalent.presentations

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.setianjay.mydigitalent.R
import com.setianjay.mydigitalent.databinding.ActivityRegisterBinding
import com.setianjay.mydigitalent.datas.source.local.persistence.model.UserArg
import com.setianjay.mydigitalent.datas.source.local.persistence.sqlite.SqliteHelper
import com.setianjay.mydigitalent.enums.Gender
import com.setianjay.mydigitalent.models.User
import com.setianjay.mydigitalent.utils.Constant
import com.setianjay.mydigitalent.utils.ViewUtil
import com.setianjay.mydigitalent.utils.capitalFirstChar
import com.setianjay.mydigitalent.utils.hasDefaultImage
import kotlinx.coroutines.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private var latitude: String = ""
    private var longitude: String = ""
    private var selectedPhotoUri: Uri? = null
    private val sqliteHelper by lazy { SqliteHelper(this@RegisterActivity) }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let {
                selectedPhotoUri = it.data
                try {
                    selectedPhotoUri?.let { uri ->
                        val bitmap = ViewUtil.getBitmapFromUri(this.contentResolver, uri)
                        binding.ivUser.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initActionBar()
        initListener()
        getLocation()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbarRegistration)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initListener() {
        binding.apply {
            btnChooseImage.setOnClickListener(this@RegisterActivity)
            btnRegister.setOnClickListener(this@RegisterActivity)
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocation = LocationServices.getFusedLocationProviderClient(this)
            fusedLocation.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    latitude = it.latitude.toString()
                    longitude = it.longitude.toString()
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_choose_image -> {
                val gallery = Intent().apply {
                    type = IMAGE_TYPE
                    action = Intent.ACTION_GET_CONTENT
                }

                resultLauncher.launch(gallery)
            }
            R.id.btn_register -> {
                var isAllPassed = true


                // check whether image still use the default image
                if (binding.ivUser.hasDefaultImage()) {
                    binding.ivPhotoNotificationError.visibility = View.VISIBLE
                    isAllPassed = false
                } else {
                    binding.ivPhotoNotificationError.visibility = View.GONE
                }

                // check whether username is blank
                if (binding.etUsername.text.isEmpty()) {
                    binding.etUsername.error =
                        getString(
                            R.string.form_input_empty,
                            Constant.FormInput.USERNAME.capitalFirstChar()
                        )
                    isAllPassed = false
                }

                // check whether address is blank
                if (binding.etAddress.text.isBlank()) {
                    binding.etAddress.error = getString(
                        R.string.form_input_empty,
                        Constant.FormInput.ADDRESS.capitalFirstChar()
                    )
                    isAllPassed = false
                }

                // check whether phone is blank
                if (binding.etPhone.text.isBlank()) {
                    binding.etPhone.error = getString(
                        R.string.form_input_empty,
                        Constant.FormInput.PHONE.capitalFirstChar()
                    )
                    isAllPassed = false
                }

                // check whether password is blank
                if (binding.etPassword.text.isBlank()) {
                    binding.etPassword.error = getString(
                        R.string.form_input_empty,
                        Constant.FormInput.PASSWORD.capitalFirstChar()
                    )
                    isAllPassed = false
                }

                // check whether radio button is checked
                if (binding.rgGender.checkedRadioButtonId == -1) {
                    binding.ivGenderNotificationError.visibility = View.VISIBLE
                } else {
                    binding.ivGenderNotificationError.visibility = View.GONE
                }


                // all condition passed
                if (isAllPassed && latitude.isNotBlank() && longitude.isNotBlank()) {
                    // get all value
                    val photoUri = selectedPhotoUri.toString()
                    val username = binding.etUsername.text.toString().lowercase().trim()
                    val address = binding.etAddress.text.toString().trim()
                    val phone = binding.etPhone.text.toString().trim()
                    val gender = if (binding.rgGender.checkedRadioButtonId == R.id.rb_male)
                        Gender.MALE else Gender.FEMALE
                    val password = binding.etPassword.text.toString().trim()
                    val location = User.Location(latitude, longitude)

                    /* check whether user is exist on database
                    * 1. if user don't exist process the registration
                    * 2. if exist show notification user is already register
                    * */
                    coroutineScope.launch {
                        lateinit var message: String
                        var result = REGISTER_FAILED
                        if (!sqliteHelper.isUserExist(username)) {
                            val userArg = UserArg(
                                username,
                                address,
                                phone,
                                gender,
                                password,
                                photoUri,
                                location
                            )

                            if (sqliteHelper.addUser(userArg) != -1L) {
                                message = getString(R.string.user_successfully_register)
                                result = REGISTER_SUCCESS
                            }else{
                                message = getString(R.string.input_process_error)
                            }
                        } else {
                            message = getString(R.string.user_has_already_register)
                        }

                        withContext(Dispatchers.Main){
                            Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show()
                            if(result == 0){
                                Intent(this@RegisterActivity, SplashActivity::class.java).also {
                                    startActivity(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        private const val IMAGE_TYPE = "image/*"
        private const val REGISTER_SUCCESS = 0
        private const val REGISTER_FAILED = -1
    }
}