package com.nkrumahsarpong.jsrdn

class Show (
    val id:String,
    val category:String,
    val title:String,
    val description:String,
    val content:Content
)

class Content(
    val video: String,
    val thumbnail: String
)