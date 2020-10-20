package com.apps.movifreak.Home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.apps.movifreak.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_youtube_player.*

class YoutubePlayerActivity : YouTubeBaseActivity() {

    private val TAG:String? = YoutubePlayerActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        var videoPath:String = intent.getStringExtra("key");
        Log.d(TAG, "video key: $videoPath")

        youtube_player.initialize(getString(R.string.youtube_player_api_key),
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
                        if (!b) {
                            youTubePlayer.loadVideo(videoPath)
                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                        }
                    }

                    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, youTubeInitializationResult: YouTubeInitializationResult?) {
//                        Toast.makeText(this@YoutubePlayerActivity, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show()

                        val youtubeLink = "https://www.youtube.com/watch?v=$videoPath"
                        val youtubeUri: Uri = Uri.parse(youtubeLink);
                        val trailerIntent: Intent = Intent(Intent.ACTION_VIEW, youtubeUri);
                        if (trailerIntent.resolveActivity(this@YoutubePlayerActivity.packageManager) != null) {
                            this@YoutubePlayerActivity.startActivity(trailerIntent);
                        }

                    }

                })

    }
}