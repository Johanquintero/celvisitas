package com.celar.celvisitas.ui.home

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.celar.celvisitas.R
import com.celar.celvisitas.Tools.AppConfig
import com.celar.celvisitas.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
        val texName:TextView = binding.nameRequestCard
        val imgViewRequest:ImageView = binding.imageView2
        val uriImg:Uri = Uri.parse("https://www.tsensor.online/wp-content/uploads/2020/04/avatar-icon-png-105-images-in-collection-page-3-avatarpng-512_512.png")
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            texName.text = "Juan carlos cardona"
            imgViewRequest.setImageResource(R.drawable.avatar);
        })
        return root
    }

    override fun onStart() {
        super.onStart()

        var card:FrameLayout = binding.cardRequest
        var visitList = AppConfig.visitArray;

        if(visitList.size == 0){

            homeViewModel.text.observe(viewLifecycleOwner, Observer {
                card.isInvisible

            })

        }

        Log.i("Start",visitList.size.toString())

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}