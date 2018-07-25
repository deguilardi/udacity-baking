package com.guilardi.baking.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.guilardi.baking.R;
import com.guilardi.baking.StepDetailFragment;
import com.guilardi.baking.custom.MyActivity;
import com.guilardi.baking.utilities.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends MyActivity implements MyActivity.OnOrientationChangedListener {

    @BindView(R.id.detail_toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setOrientationChangedListener(this);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // set title
        mToolbar.setTitle(getCurrentStep().getShortDescription());

        if (savedInstanceState == null) {
            StepDetailFragment fragment = new StepDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent stepsActivityIntent = new Intent(StepDetailActivity.this, StepsListActivity.class);
            stepsActivityIntent.putExtra(ARG_RECIPE, getCurrentRecipe());
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

    @Override
    public void onOrientationChanged(int newOrientation) {
        if(newOrientation == Configuration.ORIENTATION_LANDSCAPE && Helper.isPhone(this)){
            Helper.gotoRecipeStep(this, getCurrentRecipe(), getCurrentStepPosition());
        }
    }
}
