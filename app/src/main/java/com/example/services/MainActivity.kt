package com.example.services

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.services.interfaces.MyInterface
import com.example.services.reciever.MyBroadcast
import com.example.services.services.MyBackgroundService

class MainActivity() : AppCompatActivity(), MyInterface {

    lateinit var tvData: TextView

    lateinit var broadcast: BroadcastReceiver
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvData = findViewById<TextView>(R.id.tv_data)

        broadcast = MyBroadcast(this)

        val intentFilter = IntentFilter("com.example.mynotes.ACTION_SEND")
        registerReceiver(broadcast, intentFilter)

        val serviceIntent = Intent(this, MyBackgroundService::class.java)
        startService(serviceIntent)

        val textData = intent.getStringExtra("key")
        if (!textData.isNullOrEmpty()){
            tvData.text = textData.toString()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcast)
    }

    override fun sendData(data: String?) {
        tvData.text = data
    }
}