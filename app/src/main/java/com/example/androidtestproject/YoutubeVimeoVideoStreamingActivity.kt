package com.example.androidtestproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment


class YoutubeVimeoVideoStreamingActivity : AppCompatActivity() {

    private var videoPlayer: SimpleExoPlayer? = null

    private val YOUTUBE_API_KEY = "AIzaSyBZusYKKoym7fnVGKZnmM2keES489_Ik7Q"
    var videoDuration = 0
    val videoUrl = "https://player.vimeo.com/video/425396315"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_vimeo_video_streaming)
        //------------using vimeo with custom library (https://github.com/ct7ct7ct7/Android-VimeoPlayer)-------------------
        //need AppCompatActivity doesn't work with YoutubeBaseActivity
        /*lifecycle.addObserver(vimeo_custom_library)
        vimeo_custom_library.initialize(true, 49462103)
*/
        //------------using exo player to view vimeo object-------------------
        /* val retrofit = Retrofit.Builder()
             .baseUrl("https://player.vimeo.com/video/")
             .addConverterFactory(GsonConverterFactory.create())
             .build()
         val vimeoVideoApi = retrofit.create(VimeoVideoWebService::class.java)
         CoroutineScope(Dispatchers.Main).launch {
             val result = vimeoVideoApi.getVideoConfig()
             initializePlayer(result.request.files.progressive[0].url)
         }*/

        //------------playing vemio video using a webview-------------------
//        webView1.settings.javaScriptEnabled = true
//        webView1.loadData("<iframe src=\"$videoUrl\" width=\"100%\" height=\"100%\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>", "text/html", "utf-8")

        //------------using youtube github custom library (https://github.com/PierfrancescoSoffritti/android-youtube-player)-------------------
        //need AppCompatActivity doesn't work with YoutubeBaseActivity
//        lifecycle.addObserver(youtube_player_view)
        /*custome web frame for youtube web view not the library ui
        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(0)
            .rel(1)
            .ivLoadPolicy(3)
            .ccLoadPolicy(0)
            .build()
        youtube_player_view.enableAutomaticInitialization = false
        youtube_player_view.initialize(youtubeListener,true,iFramePlayerOptions) */

//        youtube_player_view.addYouTubePlayerListener(youtubeListener)

        //------------using youtube offical library for android-------------------
        // the activity must extend YouTubeBaseActivity So that this library can work
        /* offical_youtube_player_lib_view.initialize(YOUTUBE_API_KEY,
             object : com.google.android.youtube.player.YouTubePlayer.OnInitializedListener {
                 override fun onInitializationSuccess(
                     p0: com.google.android.youtube.player.YouTubePlayer.Provider?,
                     p1: com.google.android.youtube.player.YouTubePlayer?,
                     p2: Boolean
                 ) {
                     p1?.setPlayerStyle(com.google.android.youtube.player.YouTubePlayer.PlayerStyle.MINIMAL)
                     p1?.cueVideo("668nUCeBHyY")
                     p1?.setPlayerStateChangeListener(object :
                         com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener {
                         override fun onLoading() {}
                         override fun onLoaded(p0: String?) {}
                         override fun onAdStarted() {}
                         override fun onVideoStarted() {}
                         override fun onVideoEnded() {
                             p1.play()
                             p1.pause()
                         }

                         override fun onError(p0: com.google.android.youtube.player.YouTubePlayer.ErrorReason?) {}
                     })

                 }

                 override fun onInitializationFailure(
                     p0: com.google.android.youtube.player.YouTubePlayer.Provider?,
                     p1: YouTubeInitializationResult?
                 ) {

                 }
             })*/

        //------------using youtube with fragment-------------------
        val youtubePLayerFragment1: YouTubePlayerSupportFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_youtube_player) as YouTubePlayerSupportFragment
        //this code works even if this initialize method report error (Something related to the new androidx library and Google hasn't refactored youtube api library to androidx)
        youtubePLayerFragment1.initialize(YOUTUBE_API_KEY,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubePlayer?,
                    p2: Boolean
                ) {
                    p1?.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
                    p1?.loadVideo("668nUCeBHyY")
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                }

            })

    }

    //----------------- for youtube github custom library
    /*private val youtubeListener = object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            super.onReady(youTubePlayer)
            youTubePlayer.cueVideo("50VNCymT-Cs", 0f)
        }

        // the 3 methods below is to prevent user from seeing related videos at the end
        override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
            super.onCurrentSecond(youTubePlayer, second)
            if (second.toInt() == (videoDuration - 1)) {
                youTubePlayer.pause()
                youTubePlayer.seekTo(0f)
            }
        }

        override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
            super.onVideoDuration(youTubePlayer, duration)
            videoDuration = duration.toInt()


        }

        override fun onStateChange(
            youTubePlayer: YouTubePlayer,
            state: PlayerConstants.PlayerState
        ) {
            super.onStateChange(youTubePlayer, state)
            if (state == PlayerConstants.PlayerState.ENDED){
                youTubePlayer.pause()
                youTubePlayer.seekTo(0f)
            }
        }*/

    //----------------- for vimeo streaming with exo player
    /* private fun initializePlayer(videoUrl: String) {
         videoPlayer = SimpleExoPlayer.Builder(this).build()
         pv_exoplayer.player = videoPlayer
         buildMediaSource(videoUrl)?.let {
             videoPlayer?.prepare(it)
         }
     }
 
     private fun buildMediaSource(videoUrl: String): MediaSource? {
         val dataSourceFactory = DefaultDataSourceFactory(this, "sample")
         return ProgressiveMediaSource.Factory(dataSourceFactory)
             .createMediaSource(Uri.parse(videoUrl))
     }
 
     override fun onResume() {
         super.onResume()
         videoPlayer?.playWhenReady = true
     }
 
     override fun onStop() {
         super.onStop()
         videoPlayer?.playWhenReady = false
         if (isFinishing) {
             releasePlayer()
         }
     }
 
     private fun releasePlayer() {
         videoPlayer?.release()
     }
 
     interface VimeoVideoWebService {
 //        @GET("425396315/config")
 //        @GET("76979871/config")
 //        @GET("66865270/config")
         @GET("49462103/config")
         suspend fun getVideoConfig(): VimeoVideoResponse
     }
 
     data class VimeoVideoResponse(
         @SerializedName("request")
         val request: Request
     ) {
         data class Request(
             @SerializedName("files")
             val files: Files
         ) {
             data class Files(
                 @SerializedName("progressive")
                 val progressive: List<Progressive>
             ) {
                 data class Progressive(
                     @SerializedName("profile")
                     val profile: Int,
                     @SerializedName("width")
                     val width: Int,
                     @SerializedName("mime")
                     val mime: String,
                     @SerializedName("fps")
                     val fps: Int,
                     @SerializedName("url")
                     val url: String,
                     @SerializedName("cdn")
                     val cdn: String,
                     @SerializedName("quality")
                     val quality: String,
                     @SerializedName("id")
                     val id: String,
                     @SerializedName("origin")
                     val origin: String,
                     @SerializedName("height")
                     val height: Int
                 )
             }
         }
 
     }*/
}

