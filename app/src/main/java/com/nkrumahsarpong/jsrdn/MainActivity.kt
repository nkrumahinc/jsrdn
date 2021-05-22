package com.nkrumahsarpong.jsrdn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.multidex.MultiDex
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val STATE_RESUME_WINDOW = "resumeWindow"
const val STATE_RESUME_POSITION = "resumePosition"
const val STATE_PLAYER_FULLSCREEN = "playerFullscreen"
const val STATE_PLAYER_PLAYING = "playerOnPlay"


class MainActivity : AppCompatActivity(), ShowClickListener {

    private var adTag: String? = null
    private var adsLoader: ImaAdsLoader? = null
    private var player: SimpleExoPlayer? = null

    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: SimpleExoPlayer

    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvCategories:RecyclerView = findViewById(R.id.rvCategories)
        playerView = findViewById(R.id.playerview)

        MultiDex.install(this@MainActivity)
        adsLoader = ImaAdsLoader.Builder(this@MainActivity).build()

        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            isFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
            isPlayerPlaying = savedInstanceState.getBoolean(STATE_PLAYER_PLAYING)
        }

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

                    adTag = response.body()!!.adtag

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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, exoPlayer.currentWindowIndex)
        outState.putLong(STATE_RESUME_POSITION, exoPlayer.currentPosition)
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, isFullscreen)
        outState.putBoolean(STATE_PLAYER_PLAYING, isPlayerPlaying)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
            playerView.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {
            initializePlayer()
            playerView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    private fun releasePlayer(){
        isPlayerPlaying = exoPlayer.playWhenReady
        playbackPosition = exoPlayer.currentPosition
        currentWindow = exoPlayer.currentWindowIndex
        exoPlayer.release()
    }



    override fun onShowClick(show: Show) {
        val url:String = "https:" + show.content.video.split(":")[1]
        initializePlayer(url)
    }

    private fun initializePlayer(url:String = ""){
        // set up the factory for media sources, passing the ads loader and ad view providers
        val dataSourceFactory =
            DefaultDataSourceFactory(
                this, Util.getUserAgent(this, getString(R.string.app_name)))
        val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
            .setAdsLoaderProvider{adsLoader}
            .setAdViewProvider(playerView)
        player = SimpleExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory).build()
        adsLoader?.setPlayer(player)

        val mediaItem = MediaItem.Builder().apply{
            setAdTagUri(adTag)
            setUri(url)
        }
        player?.setMediaItem(mediaItem.build())
        player?.prepare()
        player?.playWhenReady = true

        exoPlayer = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = isPlayerPlaying
            seekTo(currentWindow, playbackPosition)
            setMediaItem(mediaItem.build(), false)
            prepare()
        }

        playerView.player = exoPlayer

    }


}