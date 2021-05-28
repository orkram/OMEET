package com.example.orangemeet.activities


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.orangemeet.BackendCommunication
import com.example.orangemeet.CustomJitsiFragment
import com.example.orangemeet.R
import com.example.orangemeet.UserInfo
import com.example.orangemeet.ui.login.LoginActivity
import com.facebook.react.modules.core.PermissionListener
import com.google.android.material.navigation.NavigationView
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface


class MainActivity : AppCompatActivity(), JitsiMeetActivityInterface {

    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var customJitsiFragment: CustomJitsiFragment
    lateinit var customJitsiFragmentView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextAppearance(applicationContext, R.style.title_bar)

        val logoutButton = findViewById<Button>(R.id.logout)
        logoutButton.setOnClickListener {
            goToLoginActivity()
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val headerView = navView.getHeaderView(0)
        val menuUsername : TextView = headerView.findViewById(R.id.menu_username)
        menuUsername.text = UserInfo.userName
        val menuEmail : TextView = headerView.findViewById(R.id.menu_email)
        menuEmail.text = UserInfo.userEmail
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,
            R.id.nav_contacts,
            R.id.nav_settings,
            R.id.nav_meetings,
            R.id.nav_video,
            R.id.nav_calendar
        ), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        customJitsiFragment = supportFragmentManager.findFragmentById(R.id.jitsiFragment) as CustomJitsiFragment
        customJitsiFragmentView = findViewById(R.id.jitsiFragment)
        customJitsiFragmentView.visibility = View.GONE
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun goToLoginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        BackendCommunication.logout(
            applicationContext
        ) {
            startActivity(intent)
            finish()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        println(if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) "Night" else "Day")
    }

    override fun requestPermissions(permissions: Array<out String>?, requestCode: Int, listener: PermissionListener?) {
        JitsiMeetActivityDelegate.requestPermissions(this, permissions, requestCode, listener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}