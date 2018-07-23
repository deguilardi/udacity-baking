package com.guilardi.baking.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.guilardi.baking.R;
import com.guilardi.baking.StepDetailFragment;
import com.guilardi.baking.data.Recipe;
import com.guilardi.baking.utilities.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {

    public static final String ARG_RECIPE = "recipe";
    public static final String ARG_STEP_POSITION = "position";
    public static final String KEY_LAST_ORIENTATION = "lastOrientation";

    private Recipe mRecipe;
    private int mStepPosition;
    private int lastOrientation;

    @BindView(R.id.detail_toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // get the recipe
        Bundle b = this.getIntent().getExtras();
        if(b != null){
            mRecipe = (Recipe) b.get(ARG_RECIPE);
            mStepPosition = (int) b.get(ARG_STEP_POSITION);
        }
        else{
            Toast.makeText(this, "An error has occurred. Pleas try again later", Toast.LENGTH_LONG).show();
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ARG_RECIPE, getIntent().getStringExtra(ARG_RECIPE));
            arguments.putString(ARG_STEP_POSITION, getIntent().getStringExtra(ARG_STEP_POSITION));
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent stepsActivityIntent = new Intent(StepDetailActivity.this, StepsListActivity.class);
            stepsActivityIntent.putExtra(StepsListActivity.ARG_RECIPE, mRecipe);
            startActivity(stepsActivityIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title){
        super.setTitle(title);
        mToolbar.setTitle(title);
    }

    public Recipe getRecipe(){
        return mRecipe;
    }

    public Recipe.Step getStep(){
        return mRecipe.getSteps().get(mStepPosition);
    }

    public int getStepPosition(){
        return mStepPosition;
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
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation != lastOrientation) {
            onScreenOrientationChanged(currentOrientation);
            lastOrientation = currentOrientation;
        }
    }

    public void onScreenOrientationChanged(int currentOrientation) {
        if(currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            Helper.gotoRecipeStep(this, mRecipe, mStepPosition);
        }
    }
}
