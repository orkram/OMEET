package com.example.orangemeet

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response

import com.example.orangemeet.R
import com.example.orangemeet.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val accCreateButton = findViewById<Button>(R.id.register)
        val email = findViewById<EditText>(R.id.email)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val repeatPassword = findViewById<EditText>(R.id.password_repeat)

        accCreateButton.setOnClickListener{
            if(username.text.isEmpty() or email.text.isEmpty()
            or password.text.isEmpty() or repeatPassword.text.isEmpty()){
                Toast.makeText(applicationContext, R.string.fields_must_be_not_empty, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!password.text.toString().equals(repeatPassword.text.toString())){
                Toast.makeText(applicationContext, R.string.passwords_dont_match, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            BackendCommunication.Register(this,
                    email.text.toString(), "firstname", "lastname", "imgurl",
                    username.text.toString(), password.text.toString(),
                    Response.Listener {
                        goToLoginActivity()
                        Log.i("RegisterActivity", "Register success")
                    },
                    Response.ErrorListener {
                        Log.i("RegisterActivity", "Register failed")
                        Toast.makeText(applicationContext, R.string.registed_failed, Toast.LENGTH_LONG).show()
                    })

        }
    }

    private fun goToLoginActivity(){
        finish()
    }
}