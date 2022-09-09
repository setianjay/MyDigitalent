package com.setianjay.mydigitalent.presentations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.setianjay.mydigitalent.R
import com.setianjay.mydigitalent.databinding.ActivityLoginBinding
import com.setianjay.mydigitalent.datas.source.local.persistence.sqlite.SqliteHelper
import com.setianjay.mydigitalent.models.User
import com.setianjay.mydigitalent.utils.Constant.FormInput.PASSWORD
import com.setianjay.mydigitalent.utils.Constant.FormInput.USERNAME
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val job = Job()
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO + job) }
    private val sqliteHelper by lazy { SqliteHelper(this@LoginActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() {

        binding.apply {
            btnLogin.setOnClickListener(this@LoginActivity)
            tvRegister.setOnClickListener(this@LoginActivity)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                val username = binding.etUsername.text.toString().lowercase().trim()
                val password = binding.etPassword.text.toString().trim()

                if (username.isBlank()) {
                    binding.etUsername.error = getString(R.string.form_input_empty, USERNAME)
                }

                if (password.isBlank()) {
                    binding.etPassword.error = getString(R.string.form_input_empty, PASSWORD)
                }

                if (username.isNotBlank() && password.isNotBlank()) {
                    coroutineScope.launch {
                        lateinit var message: String
                        lateinit var user: User
                        var result = LOGIN_FAILED_CAUSE_NO_ACCOUNT

                        if (sqliteHelper.isUserExist(username)) {
                            val data = sqliteHelper.getUserByUsername(username)
                            data?.let { response ->
                                result = if (response.password == password) {
                                    user = response
                                    LOGIN_SUCCESS
                                } else {
                                    message = getString(R.string.password_does_not_match)
                                    LOGIN_FAILED_CAUSE_WRONG_PASSWORD
                                }
                            }
                        } else {
                            message = getString(R.string.user_dont_have_an_account)
                        }

                        withContext(Dispatchers.Main) {
                            when (result) {
                                LOGIN_SUCCESS -> {
                                    val intent =
                                        Intent(this@LoginActivity, HomeActivity::class.java).apply {
                                            putExtra(HomeActivity.EXTRA_USERNAME, user.username)
                                        }
                                    startActivity(intent)
                                    finish()
                                }
                                LOGIN_FAILED_CAUSE_WRONG_PASSWORD -> {
                                    binding.etPassword.error = message
                                }
                                LOGIN_FAILED_CAUSE_NO_ACCOUNT -> {
                                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
                }
            }
            R.id.tv_register -> {
                Intent(this@LoginActivity, RegisterActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        private const val LOGIN_SUCCESS = 0
        private const val LOGIN_FAILED_CAUSE_NO_ACCOUNT = -1
        private const val LOGIN_FAILED_CAUSE_WRONG_PASSWORD = -2
    }
}