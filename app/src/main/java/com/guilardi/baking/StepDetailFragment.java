package com.guilardi.baking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.guilardi.baking.activities.StepDetailActivity;
import com.guilardi.baking.utilities.ExoPlayerVideoHandler;
import com.guilardi.baking.utilities.Helper;

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
        else{
            ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUrl(mContext, videoUrl, mVideoPlayer);
        }
    }

    @Override
    public void onClick(View v) {
        int callerID = v.getId();
        switch (callerID){
            case R.id.btn_previous:
            case R.id.btn_next:
                int position = (callerID == R.id.btn_previous) ? mContext.getStepPosition() - 1 : mContext.getStepPosition() + 1;
                Helper.gotoRecipeStep(mContext, mContext.getRecipe(), position);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
    }
}
