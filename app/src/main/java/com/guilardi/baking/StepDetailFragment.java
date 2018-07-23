package com.guilardi.baking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.guilardi.baking.activities.StepDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link com.guilardi.baking.activities.RecipesListActivity}
 * in two-pane mode (on tablets) or a {@link com.guilardi.baking.activities.StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment implements View.OnClickListener {

    private StepDetailActivity mContext;

    @BindView(R.id.instructions) TextView mInstructions;
    @BindView(R.id.videoPlayer) PlayerView mVideoPlayer;
    @BindView(R.id.btn_previous) Button mBtnPrevious;
    @BindView(R.id.btn_next) Button mBtnNext;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (StepDetailActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        // get the recipe
        Bundle b = mContext.getIntent().getExtras();
        if(b != null){
        }
        else{
            Toast.makeText(mContext, "An error has occurred. Pleas try again later", Toast.LENGTH_LONG).show();
        }

        // buttons on click actions
        mBtnPrevious.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);

        bindValues();
        createPlayer();
        return rootView;
    }

    private void bindValues(){
        mContext.setTitle(mContext.getStep().getShortDescription());
        mInstructions.setText(mContext.getStep().getDescription());

        if(mContext.getStepPosition() + 1 == 1){
            mBtnPrevious.setEnabled(false);
        }
        else if(mContext.getStepPosition() + 1 == mContext.getRecipe().getSteps().size()){
            mBtnNext.setEnabled(false);
        }
    }

    private void createPlayer(){
        String videoUrl = mContext.getStep().getVideoURL();

        if(videoUrl.equals("")){
            mVideoPlayer.setVisibility(View.GONE);
            return;
        }

        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        Context context = getContext();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        // prepare player with the video
        Uri videoURI = Uri.parse(videoUrl);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, mContext.getString(R.string.app_name)), bandwidthMeter);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoURI);
        player.prepare(videoSource);
        player.setPlayWhenReady(true);

        // Bind the player to the view.
        mVideoPlayer.setPlayer(player);
    }

    @Override
    public void onClick(View v) {
        int callerID = v.getId();
        switch (callerID){
            case R.id.btn_previous:
            case R.id.btn_next:
                int position = (callerID == R.id.btn_previous) ? mContext.getStepPosition() - 1 : mContext.getStepPosition() + 1;
                Intent detailsActivityIntent = new Intent(mContext, StepDetailActivity.class);
                detailsActivityIntent.putExtra(StepDetailActivity.ARG_RECIPE, mContext.getRecipe());
                detailsActivityIntent.putExtra(StepDetailActivity.ARG_STEP_POSITION, position);
                startActivity(detailsActivityIntent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoPlayer.getPlayer().stop();
    }
}
