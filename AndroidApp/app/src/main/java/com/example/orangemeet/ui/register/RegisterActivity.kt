package com.example.orangemeet.ui.register

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.orangemeet.R
import com.example.orangemeet.utils.Util
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    lateinit var registerViewModel: RegisterViewModel

    lateinit var password : TextInputEditText
    lateinit var repeatPassword : TextInputEditText

    lateinit var passwordVisibilityBtn : ImageButton
    lateinit var passwordRepeatVisibilityBtn : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        //Text input fields
        val firstName = findViewById<EditText>(R.id.firstname)
        val lastName = findViewById<EditText>(R.id.lastname)
        val email = findViewById<EditText>(R.id.email)
        val username = findViewById<EditText>(R.id.username)
        password = findViewById(R.id.password)
        repeatPassword = findViewById(R.id.password_repeat)

        passwordVisibilityBtn = findViewById(R.id.passwordVisibilityBtn)
        passwordRepeatVisibilityBtn = findViewById(R.id.passwordRepeatVisibilityBtn)

        val accountCreateBtn = findViewById<Button>(R.id.register)

        registerViewModel.registerResult.observe(this,
            Observer {result ->
                if(result.success){
                    showSuccessMessage()
                    goToLoginActivity()
                }else{
                    accountCreateBtn.isEnabled = true
                    showError(result.error!!)
                }
            })

        accountCreateBtn.setOnClickListener{
            accountCreateBtn.isEnabled = false

            registerViewModel.register(email.text.toString(),
                firstName.text.toString(),
                lastName.text.toString(),
                "imgurl",
                username.text.toString(),
                password.text.toString(),
                repeatPassword.text.toString())
        }
    }

    private fun showError(@StringRes error : Int){
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    private fun showSuccessMessage(){
        Toast.makeText(applicationContext, R.string.account_create_success, Toast.LENGTH_SHORT).show()
    }

    fun showHidePassword(view : View){
        Util.showHidePassword(password, passwordVisibilityBtn, this)
    }

    fun showHideRepeatPassword(view : View){
        Util.showHidePassword(repeatPassword, passwordRepeatVisibilityBtn, this)
    }

    private fun goToLoginActivity(){
        finish()
    }
}