package com.guilardi.baking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.guilardi.baking.custom.MyActivity;
import com.guilardi.baking.data.Recipe;
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

    private MyActivity myActivity;

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
        myActivity = (MyActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        ButterKnife.bind(this, rootView);

        // buttons on click actions
        mBtnPrevious.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);

        bindValues();
        createPlayer();
        return rootView;
    }

    private void bindValues(){
        Recipe currentRecipe = myActivity.getCurrentRecipe();
        Recipe.Step currentStep = myActivity.getCurrentStep();
        int currentStepPosition = myActivity.getCurrentStepPosition();

        myActivity.setTitle(currentStep.getShortDescription());
        mInstructions.setText(currentStep.getDescription());

        if(currentStepPosition + 1 == 1){
            mBtnPrevious.setEnabled(false);
        }
        else if(currentStepPosition + 1 == currentRecipe.getSteps().size()){
            mBtnNext.setEnabled(false);
        }
    }

    private void createPlayer(){
        String videoUrl = myActivity.getCurrentStep().getVideoURL();

        if(videoUrl.equals("")){
            mVideoPlayer.setVisibility(View.GONE);
        }
        else{
            ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUrl(myActivity, videoUrl, mVideoPlayer);
        }
    }

    @Override
    public void onClick(View v) {
        int currentStepPosition = myActivity.getCurrentStepPosition();
        int callerID = v.getId();
        switch (callerID){
            case R.id.btn_previous:
            case R.id.btn_next:
                int position = (callerID == R.id.btn_previous) ? currentStepPosition - 1 : currentStepPosition + 1;
                Helper.gotoRecipeStep(myActivity, myActivity.getCurrentRecipe(), position);
                break;
        }
    }
}
