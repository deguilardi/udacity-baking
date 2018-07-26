package com.guilardi.baking.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.exoplayer2.ui.PlayerView;
import com.guilardi.baking.R;
import com.guilardi.baking.MyActivity;
import com.guilardi.baking.data.Recipe;
import com.guilardi.baking.utilities.ExoPlayerVideoHandler;
import com.guilardi.baking.utilities.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deguilardi on 7/23/18.
 *
 * show the video on fullscreen when it's a phone and in horizontal orientation only
 */

public class FullscreenVideoActivity extends MyActivity implements MyActivity.OnOrientationChangedListener {

    public static final String ARG_URL = "url";
    public static final String ARG_RECIPE = "recipe";
    public static final String ARG_STEP_POSITION = "position";

    private Recipe mRecipe;
    private int mStepPosition;
    private String mUrl;

    @BindView(R.id.videoPlayer) PlayerView mVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_video);
        ButterKnife.bind(this);
        setOrientationChangedListener(this);

        // get the url
        Bundle b = getIntent().getExtras();
        assert b != null;
        mUrl = (String) b.get(ARG_URL);
        mRecipe = (Recipe) b.get(ARG_RECIPE);
        mStepPosition = (int) b.get(ARG_STEP_POSITION);
    }

    @Override
    protected void onResume(){
        super.onResume();
        ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUrl(this, mUrl, mVideoPlayer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOrientationChanged(int newOrientation) {
        if(newOrientation == Configuration.ORIENTATION_PORTRAIT){
            Helper.gotoRecipeStep(this, mRecipe, mStepPosition);
        }
    }
}
