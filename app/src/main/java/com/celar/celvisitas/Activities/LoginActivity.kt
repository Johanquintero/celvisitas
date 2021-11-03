package com.celar.celvisitas.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.celar.celvisitas.R
import com.celar.celvisitas.Tools.AppConfig
import com.celar.celvisitas.Session.SessionManager
import com.celar.celvisitas.interfaces.LoginAPI
import com.celar.celvisitas.interfaces.VisitAPI
import com.celar.celvisitas.models.Login
import com.celar.celvisitas.models.VisitAllowedOrReject
import com.celar.celvisitas.ui.home.HomeFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.firebase.messaging.FirebaseMessaging


class LoginActivity : AppCompatActivity() {

    var username: EditText? = null
    var password: EditText? = null
    var tokenLogin: String = "";
    var nameLogin: String = ""
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(applicationContext)
        username = findViewById(R.id.userNameLogin)
        password = findViewById(R.id.passwordlogin)
    }

    fun validate(view: View) {
        if (username?.getText().toString().isEmpty()) {
            username?.setError("Este campo es requerido")
            username?.requestFocus()
            Toast.makeText(this, "EL nombre de usuario esta vacio", Toast.LENGTH_LONG)
                .show()
        } else {
            if (password?.getText().toString().isEmpty()) {
                password?.setError("Este campo es requerido")
                password?.requestFocus()
                Toast.makeText(
                    this,
                    "El campo contraseña esta vacio",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                this.LogInRequest(username?.getText().toString(), password?.getText().toString())
            }
        }

    }

    fun LogInRequest(username: String, password:String) {
        try {
            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("email", username)
            jsonObject.put("password", password)

            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()
            val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObjectString)

            val apiInterface = LoginAPI.create().logIN(requestBody = requestBody);
            apiInterface.enqueue(object : Callback<Login> {
                override fun onResponse(
                    call: Call<Login>,
                    response: Response<Login>
                ) {
                    if (response?.body() != null){
                        if (response.body()!!.success) {
                            var json: JsonObject = response.body()!!.data

                            LoginSucces(response.body()!!.success,response?.code())

                            AppConfig.userObject.put("name",json.get("name").toString())
                            AppConfig.userObject.put("token",json.get("token").toString())
                            AppConfig.USERNAME = json.get("name").toString()

                            nameLogin = json.get("name").toString()
                            tokenLogin = "Bearer ${json.get("token").toString()}"
                            AppConfig.token = tokenLogin.replace("\"", "")

                        } else {
                            LoginSucces(response.body()!!.success,response?.code())
                        }
                    }else{
                        LoginSucces(false,response?.code())
                    }
                }

                override fun onFailure(call: Call<Login>?, t: Throwable?) {
                    Log.d("RETROFIT: ", "LogInRequest error: " + t.toString())
                }
            })
        } catch (e: Exception) {
            e.stackTraceToString();
            Log.d("RETROFIT: ", "LogInRequest: " + e.toString())
        }

    }

    fun LoginSucces(authorization: Boolean,code:Int){
        if (authorization){
            notification()
            AppConfig.loggin = true
            var intent: Intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }else{
            if (code == 400){
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_LONG).show()
            }else if(code == 404){
                Toast.makeText(this, "Usuario no valido", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun notification(){

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            //Actualizo el token de firebase o noticationID
            updateTokenIdFirebase(token)

            sessionManager.createloginSession(username?.getText().toString(),password?.getText().toString(),
                nameLogin,token,tokenLogin);

            AppConfig.tokenFirebase = token

            Log.i("Firebase:",token.toString());
        })

        //Recuperar informacion de notificacion
        val url:String? = intent.getStringExtra("url")
        url?.let{
            println("Informacion de un push: ${it}")
        }


    }

    private fun updateTokenIdFirebase(token: String?) {
        val jsonObject = JSONObject()
        jsonObject.put("token", token)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()
        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObjectString)

        val apiInterface = VisitAPI.create().notificationPutId(requestBody)
            .enqueue(object : Callback<VisitAllowedOrReject> {
                override fun onResponse(
                    call: Call<VisitAllowedOrReject>,
                    response: Response<VisitAllowedOrReject>
                ) {
                    if (response?.body() != null) {
                        if (response.body()!!.success) {
                           Log.i("ResponseTokenUpdate",response.body()!!.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<VisitAllowedOrReject>?, t: Throwable?) {
                    Log.d("RETROFIT: ", "LogInRequest error: " + t.toString())
                }
            })
    }

}
