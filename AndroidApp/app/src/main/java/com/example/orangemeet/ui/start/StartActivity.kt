package com.example.orangemeet.ui.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.orangemeet.R
import com.example.orangemeet.ui.login.LoginActivity
import com.example.orangemeet.ui.main.MainActivity

class StartActivity : AppCompatActivity() {

    lateinit var startViewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        startViewModel = ViewModelProvider(this).get(StartViewModel::class.java)

        val sp = getSharedPreferences("login", Context.MODE_PRIVATE)
        if(sp.getString("username", null) == null) {
            goToLoginActivity()
            return
        } else {
            startViewModel.login(
                    sp.getString("username", "")!!,
                    sp.getString("password", "")!!
            )
        }

        startViewModel.loginResult.observe(this,
                Observer {result ->
                    if (result.success) {
                        goToMainActivity()
                    } else {
                        goToLoginActivity()
                        Toast.makeText(applicationContext, result.error!!, Toast.LENGTH_LONG).show()
                    }
                })
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}