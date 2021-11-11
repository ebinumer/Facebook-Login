package com.example.facebookloginactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setDatasFromIntent(intent.getBundleExtra("info"))
    }

    private fun setDatasFromIntent(bundleExtra: Bundle?) {

        try {
            val stringBuilder = StringBuilder()
            stringBuilder.apply {
                append(
                    "${bundleExtra?.getString("firstName")}"
                )
                append(
                    "${bundleExtra?.getString("lastName")}"
                )
            }
            textView.text = stringBuilder.toString()
            textView2.text = "${bundleExtra?.getString("email")}"
            val url = "${bundleExtra?.getString("image")}"

            Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.com_facebook_button_icon_blue)
                .into(imageView)


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}