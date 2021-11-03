package com.celar.celvisitas.Activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.celar.celvisitas.R
import com.celar.celvisitas.Tools.AppConfig
import com.celar.celvisitas.databinding.ActivityDashboardBinding
import com.celar.celvisitas.interfaces.VisitAPI
import com.celar.celvisitas.models.Visit
import com.celar.celvisitas.models.VisitAllowedOrReject
import com.celar.celvisitas.ui.home.HomeFragment
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import android.widget.EditText
import android.widget.TextView
import com.celar.celvisitas.Session.SessionManager
import com.celar.celvisitas.interfaces.RequestAllApi
import com.google.gson.JsonObject

class DashboardActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    var username: TextView? = null

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    var contexto: Context = this
    var homeFragment:HomeFragment = com.celar.celvisitas.ui.home.HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(applicationContext)
        sessionManager.checkLogin()

        AppConfig.token = sessionManager.userToken().replace("\"", "")
        AppConfig.USERNAME = sessionManager.nombre.replace("\"", "")

        setSupportActionBar(binding.appBarDashboard.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_slideshow, R.id.itemCerrarsession
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        getInfoUser()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {

        val nameText = findViewById<TextView>(R.id.name_user_nav).apply {
            text = AppConfig.USERNAME
        }

        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun  requestPermissionSend(id:String,permission:String){

        val jsonObject = JSONObject()
        jsonObject.put("status", permission)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()
        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObjectString)

        val apiInterface = VisitAPI.create().allowOrReject(id, requestBody)
            .enqueue(object : Callback<VisitAllowedOrReject> {
                override fun onResponse(
                    call: Call<VisitAllowedOrReject>,
                    response: Response<VisitAllowedOrReject>
                ) {
                    if (response?.body() != null) {
                        if (response.body()!!.success) {

                            if (homeFragment is HomeFragment) {
                                Toast.makeText(contexto,"Solicitud Confirmada",Toast.LENGTH_SHORT).show()

                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home_id, homeFragment)
                                    .commit()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<VisitAllowedOrReject>?, t: Throwable?) {
                    Log.d("RETROFIT: ", "LogInRequest error: " + t.toString())
                }
            })

        Toast.makeText(this,"Solicitud Confirmada",Toast.LENGTH_SHORT).show()

    }

    fun  requestOutPermission(id:String){

        val apiInterface = VisitAPI.create().outVisit(id)
            .enqueue(object : Callback<VisitAllowedOrReject> {
                override fun onResponse(
                    call: Call<VisitAllowedOrReject>,
                    response: Response<VisitAllowedOrReject>
                ) {
                    if (response?.body() != null) {
                        if (response.body()!!.success) {
                            Toast.makeText(contexto,"Salida Aprobada",Toast.LENGTH_SHORT).show()

                            if (homeFragment is HomeFragment) {
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home_id, homeFragment)
                                    .commit()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<VisitAllowedOrReject>?, t: Throwable?) {
                    Log.d("RETROFIT: ", "LogInRequest error: " + t.toString())
                }
            })

    }

    fun logOutSession(){
        sessionManager.logoutUser()
        Toast.makeText(this,"Cerrando session..",Toast.LENGTH_SHORT).show()
    }

    fun getInfoUser(){

        val apiInterface = RequestAllApi.create().getUser()
            .enqueue(object : Callback<VisitAllowedOrReject> {
                override fun onResponse(
                    call: Call<VisitAllowedOrReject>,
                    response: Response<VisitAllowedOrReject>
                ) {
                    if (response?.body() != null) {
                        if (response.body()!!.success) {
                            val data: JsonObject = response.body()!!.data
                                AppConfig.USERNAME = data.get("name").toString().replace("\"", "")
                                AppConfig.HOUSE = data.get("house").toString().replace("\"", "")
                                AppConfig.RESIDENCIA = data.get("residencial").toString().replace("\"", "")
                                AppConfig.EMAIL = data.get("email").toString().replace("\"", "")
                        }
                    }
                }

                override fun onFailure(call: Call<VisitAllowedOrReject>?, t: Throwable?) {
                    Log.d("RETROFIT: ", "LogInRequest error: " + t.toString())
                }
            })
    }

//    fun reloadFragmentHome(){
//        if (homeFragment is HomeFragment) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_home_id, homeFragment)
//                .commit()
//        }
//    }

}