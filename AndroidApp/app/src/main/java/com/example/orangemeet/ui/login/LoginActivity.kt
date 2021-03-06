//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej

package com.example.orangemeet.ui.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

import com.example.orangemeet.R
import com.example.orangemeet.UserInfo
import com.example.orangemeet.utils.Util
import com.example.orangemeet.ui.main.MainActivity
import com.example.orangemeet.ui.register.RegisterActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    lateinit var usernameEditText : TextInputEditText
    lateinit var passwordEditText : TextInputEditText
    lateinit var visibilityButton : ImageButton
    lateinit var loginButton : Button
    lateinit var registerButton: Button
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this)))
            .get(LoginViewModel::class.java)

        visibilityButton = findViewById(R.id.visibilityButton)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)
        registerButton = findViewById(R.id.register)
        progressBar = findViewById(R.id.progressBar)

        loginButton.isEnabled = false

        loginViewModel.loginFormState.observe(this,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }

                loginButton.isEnabled = loginFormState.isDataValid
            })

        loginViewModel.loginResult.observe(this,
            Observer { loginResult ->
                loginResult ?: return@Observer
                if(loginResult.success){
                    val loggedInUser = loginResult.data!!
                    UserInfo.userName = loggedInUser.username
                    UserInfo.userEmail = loggedInUser.email
                    setUserCredentials(loggedInUser.username, loggedInUser.password)
                    goToMainActivity()
                }else{
                    progressBar.visibility = View.GONE
                    showLoginFailed(loginResult.error!!)
                    loginButton.isEnabled = true
                    registerButton.isEnabled = true
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { } // ignore
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { } // ignore

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }

        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
            }
            false
        }

        loginButton.setOnClickListener {
            if(loginViewModel.loginFormState.value == null)
                return@setOnClickListener

            loginButton.isEnabled = false
            registerButton.isEnabled = false

            if(loginViewModel.loginFormState.value!!.isDataValid){
                login()
            }
        }

        registerButton.setOnClickListener {
            goToRegisterActivity()
        }
    }

    private fun setUserCredentials(username : String, password : String) {
        val sp = getSharedPreferences("login", Context.MODE_PRIVATE)
        with(sp.edit()){
            putString("username", username)
            putString("password", password)
            apply()
        }
    }

    private fun login(){
        progressBar.visibility = View.VISIBLE
        loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
        )
    }

    private fun goToMainActivity()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToRegisterActivity()
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    fun showHidePassword(view : View){
        Util.showHidePassword(passwordEditText, visibilityButton, this)
    }
}