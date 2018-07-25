package com.guilardi.baking.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.guilardi.baking.data.Recipe;

/**
 * Created by deguilardi on 7/25/18.
 *
 * this class handles basic common functionalities like:
 * . screen rotation
 */

public abstract class MyActivity extends AppCompatActivity {

    // orientation change consts and vars
    private static final String KEY_LAST_ORIENTATION = "lastOrientation";
    private int lastOrientation;
    private OnOrientationChangedListener onOrientationChangedListener;

    // recipe and steps consts vars
    public static final String ARG_RECIPE = "recipe";
    public static final String ARG_STEP_POSITION = "position";
    private int mCurrentStepPosition;
    private Recipe mCurrentRecipe;
    private Recipe.Step mCurrentStep;

    public interface OnOrientationChangedListener{
        void onOrientationChanged(int newOrientation);
    }

    protected void setOrientationChangedListener(OnOrientationChangedListener listener){
        onOrientationChangedListener = listener;
    }

    public Recipe getCurrentRecipe(){
        return mCurrentRecipe;
    }

    public Recipe.Step getCurrentStep(){
        return mCurrentStep;
    }

    public int getCurrentStepPosition(){
        return mCurrentStepPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the recipe
        Bundle b = this.getIntent().getExtras();
        if(b != null){
            mCurrentRecipe = (Recipe) b.get(ARG_RECIPE);
            if(b.get(ARG_STEP_POSITION) != null) {
                mCurrentStepPosition = (int) b.get(ARG_STEP_POSITION);
                mCurrentStep = mCurrentRecipe.getSteps().get(mCurrentStepPosition);
            }
        }

        if (savedInstanceState == null) {
            lastOrientation = getResources().getConfiguration().orientation;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrientationChanged();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOrientation = savedInstanceState.getInt(KEY_LAST_ORIENTATION);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_LAST_ORIENTATION, lastOrientation);
    }

    private void checkOrientationChanged() {
        int newOrientation = getResources().getConfiguration().orientation;
        if (newOrientation != lastOrientation) {
            if(onOrientationChangedListener != null) {
                onOrientationChangedListener.onOrientationChanged(newOrientation);
            }
            lastOrientation = newOrientation;
        }
    }
}
