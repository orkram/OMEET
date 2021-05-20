package com.example.orangemeet.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.example.orangemeet.BackendCommunication
import com.example.orangemeet.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val accCreateButton = findViewById<Button>(R.id.register)
        val firstName = findViewById<EditText>(R.id.firstname)
        val lastName = findViewById<EditText>(R.id.lastname)
        val email = findViewById<EditText>(R.id.email)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val repeatPassword = findViewById<EditText>(R.id.password_repeat)

        accCreateButton.setOnClickListener{
            if(username.text.isEmpty() or email.text.isEmpty()
            or password.text.isEmpty() or repeatPassword.text.isEmpty()){
                Toast.makeText(applicationContext,
                    R.string.fields_must_be_not_empty, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()){
                Toast.makeText(applicationContext,
                        R.string.email_not_correct, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(password.text.toString() != repeatPassword.text.toString()){
                Toast.makeText(applicationContext,
                    R.string.passwords_dont_match, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            BackendCommunication.register(this,
                email.text.toString(),
                firstName.text.toString(),
                lastName.text.toString(),
                "imgurl",
                username.text.toString(),
                password.text.toString(),
                Response.Listener {
                    goToLoginActivity()
                },
                Response.ErrorListener {
                    Toast.makeText(
                        applicationContext,
                        R.string.registed_failed,
                        Toast.LENGTH_LONG
                    ).show()
                })

        }
    }

    private fun goToLoginActivity(){
        finish()
    }
}