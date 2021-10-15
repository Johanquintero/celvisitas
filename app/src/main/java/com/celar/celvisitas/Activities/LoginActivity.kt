package com.celar.celvisitas.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.celar.celvisitas.R
import com.celar.celvisitas.Tools.AppConfig
import com.celar.celvisitas.Tools.SessionManager
import com.celar.celvisitas.interfaces.LoginAPI
import com.celar.celvisitas.models.Login
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
    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        AppConfig.session = SessionManager(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
                            LoginSucces(response.body()!!.success,response?.code())
                            var json: JsonObject = response.body()!!.data

                            AppConfig.userObject.put("name",json.get("name").toString())
                            AppConfig.userObject.put("token",json.get("token").toString())

                            AppConfig.USERNAME = json.get("name").toString()
                            AppConfig.token = json.get("token").toString()

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


//                        val apiInterface = LoginAPI.create().getCasas();
//                        apiInterface.enqueue(object : Callback<List<Login>> {
//                                override fun onResponse(
//                                        call: Call<List<Login>>,
//                                        response: retrofit2.Response<List<Login>>
//                                ) {
//                                        if (response?.body() != null)
//                                                response.body()!!
//                                }
//
//                                override fun onFailure(call: Call<List<Login>>?, t: Throwable?) {
//                                        Log.d("RETROFIT: ", "LogInRequest error: " + t.toString())
//                                }
//                        })
        } catch (e: Exception) {
            e.stackTraceToString();
            Log.d("RETROFIT: ", "LogInRequest: " + e.toString())
        }

    }

    fun LoginSucces(authorization: Boolean,code:Int){
        if (authorization){
            AppConfig.loggin = true
            Toast.makeText(this, "EL login fue exitoso", Toast.LENGTH_LONG).show()
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

}
