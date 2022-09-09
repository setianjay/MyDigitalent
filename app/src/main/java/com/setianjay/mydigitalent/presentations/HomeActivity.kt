package com.setianjay.mydigitalent.presentations

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.setianjay.mydigitalent.R
import com.setianjay.mydigitalent.databinding.ActivityHomeBinding
import com.setianjay.mydigitalent.datas.source.local.persistence.sqlite.SqliteHelper
import com.setianjay.mydigitalent.utils.Constant
import kotlinx.coroutines.*

class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val job = Job()
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO + job) }
    private val sqliteHelper by lazy { SqliteHelper(this@HomeActivity) }

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initData()
        initListener()
    }

    private fun initData() {
        username = intent.getStringExtra(EXTRA_USERNAME)
        username?.let {
            initView(it)
        }
    }

    private fun initView(username: String) {
        coroutineScope.launch {
            val dataUser = sqliteHelper.getUserByUsername(username)
            if (dataUser != null) {
                withContext(Dispatchers.Main) {
                    binding.apply {
                        ivUserProfile.setImageBitmap(dataUser.photo)
                        tvUserUsername.text = dataUser.username
                        ivUserGender.setImageResource(
                            if (dataUser.gender.value == Constant.Gender.MALE) R.drawable.male_gender
                            else R.drawable.female_gender
                        )
                        tvUserAddress.text = getString(R.string.info_address, dataUser.address)
                        tvUserPhone.text = getString(R.string.info_phone, dataUser.phone)
                        tvUserLatitude.text = getString(R.string.info_latitude, dataUser.location.latitude)
                        tvUserLongitude.text = getString(R.string.info_longitude, dataUser.location.longitude)
                    }
                }
            }
        }
    }

    private fun initListener(){
        binding.btnLogout.setOnClickListener {
            Intent(this@HomeActivity, LoginActivity::class.java).also{
                startActivity(it)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
}