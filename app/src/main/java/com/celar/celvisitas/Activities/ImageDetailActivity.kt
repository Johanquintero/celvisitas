package com.celar.celvisitas.Activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.celar.celvisitas.R

import com.celar.celvisitas.databinding.ActivityImageDetailBinding
import com.celar.celvisitas.ui.home.RoundedCornersTransformation
import com.celar.celvisitas.ui.home.VisitAdapter.EXTRA_MESSAGE

class ImageDetailActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ImageDetailActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        val uriPhoto = intent.getStringExtra(EXTRA_MESSAGE)
        val name = intent.getStringExtra("nameUser")

        val imgView:ImageView = findViewById(R.id.imageUserZoomid)
        Glide.with(this).load(uriPhoto).placeholder(R.drawable.avatar).apply(
            RequestOptions.bitmapTransform(
             RoundedCornersTransformation(this, 15, 2)
            )).into(imgView)

        val nameText = findViewById<TextView>(R.id.textNameDetailPhoto).apply {
            text = name
        }

//        val imgView = findViewById<ImageView>(R.id.imageUserZoomid).apply {
//            Glide.with(this).load(uriPhoto).centerCrop().placeholder(R.drawable.avatar)
//                .into(this)
//        }

    }

}