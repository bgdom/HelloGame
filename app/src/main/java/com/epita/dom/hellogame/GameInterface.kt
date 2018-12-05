package com.epita.dom.hellogame

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GameInterface {
    @GET("game/list")
    fun listGames() : Call<MutableList<Game>>

    @GET("game/details")
    fun getGameDetails(@Query("game_id") id: Int) : Call<GameInfo>
}