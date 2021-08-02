package com.rc.weatherforecastapplication.home.data.network.responsemodel


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)