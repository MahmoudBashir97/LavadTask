package com.mahmoudbashir.lavadtask.pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Source(
    @SerializedName("id")
    val id: Any,
    @SerializedName("name")
    val name: String
):Serializable