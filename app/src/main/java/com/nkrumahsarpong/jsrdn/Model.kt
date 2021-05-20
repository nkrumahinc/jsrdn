package com.nkrumahsarpong.jsrdn

data class Response(
    val adtag:String,
    val shows:List<Show>
)

data class Show(
    val id: String,
    val category: String,
    val title: String,
    val description: String,
    val content: Content
)

data class Content(
    val video: String,
    val thumbnail: String
)