package com.example.seniya_polina_lol.test2

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.internal.location.zzaz
import org.json.JSONObject
import java.net.URL
import org.jetbrains.anko.doAsync


class MainActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var user_field: EditText? = null
    private var main_btn: Button? = null
    private var result_info: TextView? = null
    private var geo_position: TextView? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user_field = findViewById(R.id.user_field)
        main_btn = findViewById(R.id.main_btn)
        result_info = findViewById(R.id.result_info)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        geo_position = findViewById(R.id.geo_position)

        main_btn?.setOnClickListener {
            if (user_field?.text?.toString()?.trim()?.equals("")!!)
                Toast.makeText(this, "enter city", Toast.LENGTH_SHORT).show()
            else {
                val city: String = user_field?.text.toString()
                val key: String = "417430b7441fdffce76cb1e020acbe97"
                //https://api.openweathermap.org/data/2.5/weather?lat=55.747747747747745&lon=49.20371801285128&exclude=now&appid=417430b7441fdffce76cb1e020acbe97&units=metric&lang=ru
                // lat={lat}&lon={lon}&exclude={part}&appid=
                val url: String =
                    "https://api.openweathermap.org/data/2.5/weather?q=$city&&APPID=$key&units=metric&lang=ru"
                // https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&exclude=now&appid=$key&units=metric&lang=ru
                doAsync {
                    val apiResponse: String = URL(url).readText()

                    val weather = JSONObject(apiResponse).getJSONArray("weather")
                    val description = weather.getJSONObject(0).getString("description")

                    val main = JSONObject(apiResponse).getJSONObject("main")
                    val temp = main.getString("temp")

                    result_info?.text = "температура: $temp\n$description"
                    //Log.d("INFO",apiResponse)
                }
            }
        }

        findViewById<Button>(R.id.but_get_loc).setOnClickListener {
            fetchLocation()
        }
    }

    @SuppressLint("SetTextI18n")
    fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation


        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                101
            )

            return
        }


        task.addOnSuccessListener {

            if (it != null) {

                println("${it.latitude} ${it.longitude}")

                val key: String = "417430b7441fdffce76cb1e020acbe97"
                val url: String =
                    " https://api.openweathermap.org/data/2.5/weather?lat=${it.latitude}&lon=${it.longitude}&exclude=now&appid=$key&units=metric&lang=ru"

                doAsync {
                    val apiResponse: String = URL(url).readText()

                    val weather = JSONObject(apiResponse).getJSONArray("weather")
                    val description = weather.getJSONObject(0).getString("description")

                    val main = JSONObject(apiResponse).getJSONObject("main")
                    val temp = main.getString("temp")

                    geo_position?.text = "температура: $temp\n$description"
                    //Log.d("INFO",apiResponse)
                }
            }
        }
    }
}





