//Autorzy kodu źródłowego: Konrad Stręk, Michał Skrok
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej

package com.example.orangemeet.ui.main


import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.orangemeet.R
import com.example.orangemeet.UserInfo
import com.example.orangemeet.data.DataRepository
import com.example.orangemeet.ui.login.LoginActivity
import com.example.orangemeet.ui.main.calling.CustomJitsiFragment
import com.facebook.drawee.drawable.RoundedBitmapDrawable
import com.facebook.react.modules.core.PermissionListener
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.CornerSize
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity(), JitsiMeetActivityInterface {

    private lateinit var mainViewModel : MainViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var customJitsiFragment: CustomJitsiFragment
    lateinit var customJitsiFragmentView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.getSettingsResult.observe(this,
                Observer {result ->
                    if(result.success){
                        val settingsJson = result.data!!
                        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                        with (prefs.edit()) {
                            putBoolean("start_with_audio", settingsJson.getBoolean("defaultMicOn"))
                            putBoolean("start_with_video", settingsJson.getBoolean("defaultCamOn"))
                            putBoolean("private_user", settingsJson.getBoolean("private"))
                            apply()
                        }
                    }else{
                        Toast.makeText(applicationContext, result.error!!, Toast.LENGTH_LONG).show()
                    }
                })

        mainViewModel.getSettings()

       /* BackendCommunication.getSettings(BackendRequestQueue.getInstance(applicationContext).requestQueue,
                Response.Listener {settingsJson ->
                    val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    with (prefs.edit()) {
                        putBoolean("start_with_audio", settingsJson.getBoolean("defaultMicOn"))
                        putBoolean("start_with_video", settingsJson.getBoolean("defaultCamOn"))
                        putBoolean("private_user", settingsJson.getBoolean("private"))
                        apply()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, "Nie udało się wczytać ustawień", Toast.LENGTH_LONG).show()
                })
*/



        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextAppearance(applicationContext, R.style.title_bar)

        val logoutButton = findViewById<Button>(R.id.logout)
        logoutButton.setOnClickListener {
            logout()
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val headerView = navView.getHeaderView(0)
        val menuUsername : TextView = headerView.findViewById(R.id.menu_username)
        menuUsername.text = mainViewModel.getLoggedInUserUsername()
        val menuEmail : TextView = headerView.findViewById(R.id.menu_email)
        menuEmail.text = mainViewModel.getLoggedInUserEmail()
        val avatarView : ShapeableImageView = headerView.findViewById(R.id.avatar)

        mainViewModel.getAvatarResult.observe(this,
            Observer {result ->
                if (result.success) {
                    avatarView.setImageBitmap(result.data!!)
                } else {
                    Toast.makeText(this, result.error!!, Toast.LENGTH_SHORT).show()
                }
            })

        mainViewModel.getAvatar()

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

    private fun logout(){
        mainViewModel.logout()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
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