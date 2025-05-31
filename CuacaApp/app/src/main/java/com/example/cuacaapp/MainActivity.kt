package com.example.cuacaapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiKey = "YOUR_API_KEY_HERE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCity = findViewById<EditText>(R.id.etCity)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val imgWeather = findViewById<ImageView>(R.id.imgWeather)

        btnSearch.setOnClickListener {
            val city = etCity.text.toString()
            if (city.isNotEmpty()) {
                RetrofitClient.instance.getWeatherByCity(city, apiKey)
                    .enqueue(object : Callback<WeatherResponse> {
                        override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                            val body = response.body()
                            if (body != null) {
                                val desc = body.weather[0].description
                                val temp = body.main.temp
                                val icon = body.weather[0].icon

                                tvResult.text = "Cuaca di ${body.name}: $desc, $temp°C"

                                val iconUrl = "https://openweathermap.org/img/wn/$icon@2x.png"
                                Glide.with(this@MainActivity)
                                    .load(iconUrl)
                                    .into(imgWeather)
                            } else {
                                tvResult.text = "Data tidak ditemukan"
                            }
                        }

                        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                            tvResult.text = "Gagal memuat data"
                        }
                    })
            } else {
                tvResult.text = "Masukkan nama kota terlebih dahulu"
            }
        }
    }
}
