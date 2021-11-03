package com.celar.celvisitas.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.celar.celvisitas.Tools.AppConfig
import com.celar.celvisitas.databinding.FragmentHomeBinding
import com.celar.celvisitas.interfaces.VisitAPI
import com.celar.celvisitas.models.Visit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.celar.celvisitas.R
import org.json.JSONArray

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var btnReload: ImageButton? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        btnReload = binding.btnReload
        val uriImg:Uri = Uri.parse("https://www.tsensor.online/wp-content/uploads/2020/04/avatar-icon-png-105-images-in-collection-page-3-avatarpng-512_512.png")
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            texName.text = "Juan carlos cardona"
//            imgViewRequest.setImageResource(R.drawable.avatar);
        })
        return root
    }

    override fun onStart() {
        super.onStart()
        apiRequestVisit()
        btnReload?.setOnClickListener(View.OnClickListener {
            apiRequestVisit()
        })
    }

    fun apiRequestVisit(){
        val apiInterface = VisitAPI.create().getVisits()
            .enqueue(object : Callback<Visit> {
                override fun onResponse(
                    call: Call<Visit>,
                    response: Response<Visit>
                ) {
                    if (response?.body() != null) {
                        if (response.body()!!.success) {

                            var visitList = response.body()!!.data
                            AppConfig.visitArray = visitList;
                            init()

                        }
                    }
                }

                override fun onFailure(call: Call<Visit>?, t: Throwable?) {
                    Log.d("RETROFIT: ", "LogInRequest error: " + t.toString())
                }
            })

        if (AppConfig.visitArray.size.toString() != "0"){
            Toast.makeText(getActivity(),"Solicitudes Activas",Toast.LENGTH_SHORT).show();
            init()
        }
    }

    fun init() {
//            se inicializa el reciclerVIew
        var x:Int = 0;
        val visitJSONArray = JSONArray(AppConfig.visitArray)
        var visits = ArrayList<VisitElement>();

        while (x < visitJSONArray.length()) {

            var id:String =visitJSONArray.getJSONObject(x).get("id").toString();
            var name:String = visitJSONArray.getJSONObject(x).get("person").toString()
            var status:String = visitJSONArray.getJSONObject(x).get("status").toString()
            var img:String = visitJSONArray.getJSONObject(x).get("photo").toString()


            if(status == "allowed" || status == "waiting")
                visits.add(VisitElement(id,name,status,img))

            x++
        }

        var recyclerView: RecyclerView = binding.listReciclerView;
        recyclerView.setLayoutManager(LinearLayoutManager(getActivity()))
        var visitAdapter: VisitAdapter =  VisitAdapter(visits, getActivity())
        recyclerView.setHasFixedSize(true)
        recyclerView.setAdapter(visitAdapter)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

