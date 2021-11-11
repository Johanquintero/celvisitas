package com.celar.celvisitas.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.celar.celvisitas.R
import com.celar.celvisitas.Session.SessionManager
import com.celar.celvisitas.Tools.AppConfig
import com.celar.celvisitas.interfaces.LoginAPI
import com.celar.celvisitas.interfaces.VisitAPI
import com.celar.celvisitas.models.Login
import com.celar.celvisitas.models.VisitAllowedOrReject
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.messaging.FirebaseMessaging

import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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


        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )

        val options = FirebaseOptions.Builder()
            .setApplicationId("1:396693274954:android:7a7ee048e65bf1d923c1b3") // Required for Analytics.
            .setProjectId("celvisitas-96a96") // Required for Firebase Installations.
            .setApiKey("AIzaSyDJLZAlEu9SNL-a4cPVf4-k4QBVgYgoilY") // Required for Auth.
            .build()
        FirebaseApp.initializeApp(this, options, "FIREBASE CelVisitas")

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
                            AppConfig.userObject.put("name",json.get("name").toString())
                            AppConfig.userObject.put("token",json.get("token").toString())
                            AppConfig.USERNAME = json.get("name").toString()

                            nameLogin = json.get("name").toString()
                            tokenLogin = "Bearer ${json.get("token").toString()}"
                            AppConfig.token = tokenLogin.replace("\"", "")
                            sessionManager.savedTokenUser(tokenLogin)

                            notification()

                            LoginSucces(response.body()!!.success,response?.code())

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
            sessionManager.createloginSession(username?.getText().toString(),password?.getText().toString(),
                nameLogin);

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
                Toast.makeText(baseContext,task.exception.toString(),Toast.LENGTH_LONG).show()
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Toast.makeText(baseContext,token.toString(),Toast.LENGTH_LONG).show()
            Log.i("Firebase:",token.toString());

            //Actualizo el token de firebase o noticationID
            updateTokenIdFirebase(token)

            AppConfig.tokenFirebase = token
        })

    }

    fun updateTokenIdFirebase(token: String?) {
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
                            Toast.makeText(baseContext,"Update Token",Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<VisitAllowedOrReject>?, t: Throwable?) {
                    Log.d("RETROFIT: ", "LogInRequest error: " + t.toString())
                }
            })
    }

}
