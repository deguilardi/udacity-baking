package com.guilardi.baking.utilities;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.SurfaceView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.guilardi.baking.R;

/**
 * Created by deguilardi on 7/23/18.
 */

public class ExoPlayerVideoHandler {
    private static ExoPlayerVideoHandler instance;

    public static ExoPlayerVideoHandler getInstance(){
        if(instance == null){
            instance = new ExoPlayerVideoHandler();
        }
        return instance;
    }
    private SimpleExoPlayer player;
    private String url;

    private ExoPlayerVideoHandler(){}

    public void prepareExoPlayerForUrl(Context context, String url, PlayerView exoPlayerView){
        if(context != null && url != null && exoPlayerView != null && !url.equals("")){
            context = context.getApplicationContext();
            Uri uri = Uri.parse(url);
            if(!url.equals(this.url) || player == null){
                this.url = url;

                // 1. Create a default TrackSelector
                Handler mainHandler = new Handler();
                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

                // 2. Create the player
                player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

                // prepare player with the video
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, context.getString(R.string.app_name)), bandwidthMeter);
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
                player.prepare(videoSource);
                player.setPlayWhenReady(true);
            }
            player.clearVideoSurface();
            player.setVideoSurfaceView((SurfaceView)exoPlayerView.getVideoSurfaceView());
            player.seekTo(player.getCurrentPosition() + 1);
            exoPlayerView.setPlayer(player);
        }
    }

    public void releaseVideoPlayer(){
        if(player != null)
        {
            player.release();
        }
        player = null;
    }
}
