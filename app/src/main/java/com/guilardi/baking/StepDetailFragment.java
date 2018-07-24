package com.guilardi.baking;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.PlayerView;
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

    public static final String ARG_RECIPE = "recipe";
    public static final String ARG_STEP_POSITION = "position";

    private Activity context;
    private Recipe mRecipe;
    private Recipe.Step mStep;
    private int mStepPosition;

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
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        // buttons on click actions
        mBtnPrevious.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);

        // get the recipe
        Bundle b = context.getIntent().getExtras();
        if(b != null){
            mRecipe = (Recipe) b.get(ARG_RECIPE);
            mStepPosition = getArguments().getInt(ARG_STEP_POSITION, -1);
            if(mStepPosition == -1) {
                mStepPosition = (int) b.get(ARG_STEP_POSITION);
            }
            mStep = mRecipe.getSteps().get(mStepPosition);
        }
        else{
            Toast.makeText(context, "An error has occurred. Pleas try again later", Toast.LENGTH_LONG).show();
        }

        bindValues();
        createPlayer();
        return rootView;
    }

    private void bindValues(){
        context.setTitle(mStep.getShortDescription());
        mInstructions.setText(mStep.getDescription());

        if(mStepPosition + 1 == 1){
            mBtnPrevious.setEnabled(false);
        }
        else if(mStepPosition + 1 == mRecipe.getSteps().size()){
            mBtnNext.setEnabled(false);
        }
    }

    private void createPlayer(){
        String videoUrl = mStep.getVideoURL();

        if(videoUrl.equals("")){
            mVideoPlayer.setVisibility(View.GONE);
            return;
        }
        else{
            ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUrl(context, videoUrl, mVideoPlayer);
        }
    }

    @Override
    public void onClick(View v) {
        int callerID = v.getId();
        switch (callerID){
            case R.id.btn_previous:
            case R.id.btn_next:
                int position = (callerID == R.id.btn_previous) ? mStepPosition - 1 : mStepPosition + 1;
                Helper.gotoRecipeStep(context, mRecipe, position);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
    }
}
