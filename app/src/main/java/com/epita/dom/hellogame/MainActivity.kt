package com.example.dom.hellogame

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.epita.dom.hellogame.Game
import com.epita.dom.hellogame.GameInterface
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val map: HashMap<Int, Game> = HashMap()

    fun downloadGames(){
        val callback = object : Callback<MutableList<Game>>{
            override fun onFailure(call: Call<MutableList<Game>>, t: Throwable) {
                game1.isEnabled = false
                game2.isEnabled = false
                game3.isEnabled = false
                game4.isEnabled = false
            }

            override fun onResponse(call: Call<MutableList<Game>>, response: Response<MutableList<Game>>) {
                if(response.code() == 200){
                    val data = response.body()
                    if(data != null) {
                        var game = data.random()
                        map.put(R.id.game1, game)
                        data.remove(game)
                        game1.text = game.name

                        game = data.random()
                        map.put(R.id.game2, game)
                        data.remove(game)
                        game2.text = game.name

                        game = data.random()
                        map.put(R.id.game3, game)
                        data.remove(game)
                        game3.text = game.name

                        game = data.random()
                        map.put(R.id.game4, game)
                        data.remove(game)
                        game4.text = game.name

                        game1.isEnabled = true
                        game2.isEnabled = true
                        game3.isEnabled = true
                        game4.isEnabled = true
                    }
                }
            }

        }

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl("https://androidlessonsapi.herokuapp.com/api/")
            .addConverterFactory(jsonConverter)
            .build()

        val gameService: GameInterface = retrofit.create(GameInterface::class.java)
        gameService.listGames().enqueue(callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        game1.isEnabled = false
        game2.isEnabled = false
        game3.isEnabled = false
        game4.isEnabled = false

        downloadGames()

        game1.setOnClickListener (this)
        game2.setOnClickListener (this)
        game3.setOnClickListener (this)
        game4.setOnClickListener (this)
    }

    override fun onClick(clickedView: View?) {
        if (clickedView != null) {
            val explicitIntent = Intent(this@MainActivity, GameInfoActivity::class.java)
            var game : Game ?= null
            if(map.containsKey(clickedView.id))
                game = map[clickedView.id]

            if(game!= null) {
                explicitIntent.putExtra("id", game.id)
                startActivity(explicitIntent)
            }
        }
    }
}
