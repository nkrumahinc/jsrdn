package com.nkrumahsarpong.jsrdn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView:RecyclerView = findViewById(R.id.recyclerView)

        val request = ServiceBuilder.buildService(Endpoint::class.java)
        val call = request.get()

        call.enqueue(object:Callback<Received>{
            override fun onFailure(call: Call<Received>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Received>, response: Response<Received>) {
                if(response.isSuccessful){
                    recyclerView.apply{
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = ShowsAdapter(response.body()!!.shows)
                    }
                }
            }

        })
    }
}