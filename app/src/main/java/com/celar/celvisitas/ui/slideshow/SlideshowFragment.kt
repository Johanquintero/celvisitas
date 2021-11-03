package com.celar.celvisitas.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.celar.celvisitas.Activities.DashboardActivity
import com.celar.celvisitas.R
import com.celar.celvisitas.Session.SessionManager
import com.celar.celvisitas.Tools.AppConfig
import com.celar.celvisitas.databinding.FragmentSlideshowBinding
import com.celar.celvisitas.interfaces.RequestAllApi
import com.celar.celvisitas.interfaces.VisitAPI
import com.celar.celvisitas.models.Visit
import com.celar.celvisitas.models.VisitAllowedOrReject
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var _binding: FragmentSlideshowBinding? = null
    private lateinit var btnLogout:Button
    var nameUser: TextView? = null
    var houseNumber: TextView? = null
    var residencial: TextView? = null
    var email: TextView? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        btnLogout = binding.buttonLogout

        nameUser = binding.nameUser
        houseNumber = binding.numberCasa
        residencial = binding.residencial
        email = binding.email

//        getInfoUser()
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            nameUser!!.text = "Nombre : "+ AppConfig.USERNAME
            houseNumber!!.text = "Casa : "+ AppConfig.HOUSE
            residencial!!.text ="Condominio : "+  AppConfig.RESIDENCIA
            email!!.text = "Email : "+ AppConfig.EMAIL
        })
        return root
    }

    override fun onResume() {
        super.onResume()
        //Evento para cerrar session
        btnLogout.setOnClickListener(View.OnClickListener {
            if (context is DashboardActivity) {
                (context as DashboardActivity).logOutSession()
            }
        })

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
                            val data:JsonObject = response.body()!!.data

                            slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
                                nameUser!!.text = "Nombre : "+data.get("name").toString().replace("\"", "")
                                houseNumber!!.text = "Casa : "+data.get("house").toString().replace("\"", "")
                                residencial!!.text = "Residencia : "+data.get("residencial").toString().replace("\"", "")
                                email!!.text = "Email : "+data.get("email").toString().replace("\"", "")
                            })

                        }
                    }
                }

                override fun onFailure(call: Call<VisitAllowedOrReject>?, t: Throwable?) {
                    Log.d("RETROFIT: ", "LogInRequest error: " + t.toString())
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}