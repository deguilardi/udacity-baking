package com.guilardi.baking.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;

import com.guilardi.baking.MyActivity;
import com.guilardi.baking.custom.NonScrollListView;
import com.guilardi.baking.data.Recipe;

import com.guilardi.baking.R;
import com.guilardi.baking.utilities.Helper;

import java.util.List;

public class StepsListActivity extends MyActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.steps_list) NonScrollListView mStepsList;
    @BindView(R.id.ingredients) TextView mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        // force landscape on tablets
        if(Helper.isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // set title
        mToolbar.setTitle(getCurrentRecipe().getName());

        setupRecyclerView();
        bindValues();
    }

    private void setupRecyclerView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        List<Recipe.Step> steps = getCurrentRecipe().getSteps();
        for(int i = 0; i< steps.size(); i++){
            adapter.add(steps.get(i).getShortDescription());
        }
        mStepsList.setAdapter(adapter);
        mStepsList.setOnItemClickListener(this);
    }

    private void bindValues(){
        StringBuilder ingredientsStr = new StringBuilder();
        List<Recipe.Ingredient> ingredients = getCurrentRecipe().getIngredients();
        for(int i = 0; i < ingredients.size(); i++){
            Recipe.Ingredient ingredient = ingredients.get(i);
            ingredientsStr.append(" - ").append(ingredient.getIngredient()).append(" : ").append(ingredient.getQuantityNormalized()).append(" ").append(ingredient.getMeasure()).append("\r\n");
        }
        mIngredients.setText(ingredientsStr.toString());
    }

    /**
     * Responds to clicks from the list.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Helper.gotoRecipeStep(this, getCurrentRecipe(), position);
    }
}
