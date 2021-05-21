package com.nkrumahsarpong.jsrdn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), ShowClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvCategories:RecyclerView = findViewById(R.id.rvCategories)

        val request = ServiceBuilder.buildService(Endpoint::class.java)
        val call = request.get()

        call.enqueue(object:Callback<Received>{
            override fun onFailure(call: Call<Received>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Received>, response: Response<Received>) {
                if(response.isSuccessful){
                    val catTitles:MutableList<String> = mutableListOf()
                    val cats:MutableList<Cat> = mutableListOf()

                    val shows = response.body()!!.shows

                    for(show in shows){
                        if (show.category !in catTitles) catTitles.add(show.category)
                    }

                    for (catTitle in catTitles){
                        val catshows:MutableList<Show> = mutableListOf()
                        for(show in shows){
                            if(show.category == catTitle) catshows.add(show)
                        }
                        cats.add(Cat(catTitle, catshows))
                    }

                    rvCategories.apply{
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = CategoriesAdapter(cats, this@MainActivity, this@MainActivity)
                    }
                }
            }

        })
    }

    override fun onShowClick(show:Show) {
        Toast.makeText(this@MainActivity, show.title, Toast.LENGTH_LONG).show()
    }


}