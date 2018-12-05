package com.example.dom.hellogame

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.epita.dom.hellogame.Game
import com.epita.dom.hellogame.GameInfo
import com.epita.dom.hellogame.GameInterface
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_game_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameInfoActivity : AppCompatActivity() {

    fun getGameInfo(id: Int){
        val callback = object : Callback<GameInfo> {
            override fun onFailure(call: Call<GameInfo>, t: Throwable) {

            }

            override fun onResponse(call: Call<GameInfo>, response: Response<GameInfo>) {
                if(response.code() == 200){
                    val data = response.body()
                    if(data != null) {
                        game_id.text = data.id.toString()
                        name.text = data.name
                        type.text = data.type

                        var desc = "";
                        if(data.description_fr != null)
                            desc = data.description_fr
                        else if(data.description_en != null)
                            desc = data.description_en
                        description.text = desc

                        nb_player.text = data.players.toString()
                        year.text = data.year.toString()

                        see_more.setOnClickListener{
                            val explicitIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.url))
                            startActivity(explicitIntent)
                        }
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
        gameService.getGameDetails(id).enqueue(callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_info)

        val origin = intent
        val id = origin.getIntExtra("id", -1)
        if(id == -1){
            finish()
            return
        }

        getGameInfo(id)
    }
}
