package com.example.practica9

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica9.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {


    var binding: ActivityMainBinding? = null
    lateinit var rvMain: RecyclerView
    lateinit var myAdapter: MyAdapter
    lateinit var searchUser: SearchView
    var BASE_URL = "https://api.github.com/"

    var retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //Inicializando Recycler view
        searchUser = findViewById(R.id.searchUser)
        rvMain = findViewById(R.id.recycler_view)
        rvMain.layoutManager = LinearLayoutManager(this)

        binding!!.searchUser.setOnQueryTextListener(this as SearchView.OnQueryTextListener)

        getAllData()
    }

    private fun searhUsers(username: String) {

        val retrodata : Call<UsersItem> = retrofit.getUser(username)
        retrodata.enqueue(object : Callback<UsersItem>{
            override fun onResponse(call: Call<UsersItem>, response: Response<UsersItem>) {
                var data = response.body()
                val user : List<UsersItem> = listOf(data) as List<UsersItem>
                myAdapter = MyAdapter(baseContext,user)
                rvMain.adapter = myAdapter
            }

            override fun onFailure(call: Call<UsersItem>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getAllData() {
        var retroData = retrofit.getData()
        retroData.enqueue(object : Callback<List<UsersItem>>{
            override fun onResponse(
                call: Call<List<UsersItem>>,
                response: Response<List<UsersItem>>
            ) {
                var data = response.body()!!
                myAdapter = MyAdapter(baseContext,data)
                rvMain.adapter = myAdapter
            }

            override fun onFailure(call: Call<List<UsersItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        if(!query.isEmpty()){
            searhUsers(query.lowercase(Locale.getDefault()))
        }
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return true
    }
}