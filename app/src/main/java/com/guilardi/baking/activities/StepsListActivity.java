package com.guilardi.baking.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;

import com.guilardi.baking.StepDetailFragment;
import com.guilardi.baking.custom.NonScrollListView;
import com.guilardi.baking.data.Recipe;

import com.guilardi.baking.R;
import com.guilardi.baking.utilities.Helper;

import java.util.List;

public class StepsListActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    public static final String ARG_RECIPE = "recipe";

    private Recipe mRecipe;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.step_detail_container) @Nullable FrameLayout mStepDetailContainer;
    @BindView(R.id.steps_list) NonScrollListView mStepsList;
    @BindView(R.id.ingredients) TextView mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // get the recipe
        Bundle b = getIntent().getExtras();
        if(b != null){
            mRecipe = (Recipe) b.get(ARG_RECIPE);
            mToolbar.setTitle(mRecipe.getName());
        }
        else{
            Toast.makeText(this, "An error has occurred. Pleas try again later", Toast.LENGTH_LONG).show();
        }

        setupRecyclerView();
        bindValues();
    }

    private void setupRecyclerView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        List<Recipe.Step> steps = mRecipe.getSteps();
        for(int i = 0; i< steps.size(); i++){
            adapter.add(steps.get(i).getShortDescription());
        }
        mStepsList.setAdapter(adapter);
        mStepsList.setOnItemClickListener(this);
    }

    private void bindValues(){
        String ingredientsStr = "";
        List<Recipe.Ingredient> ingredients = mRecipe.getIngredients();
        for(int i = 0; i < ingredients.size(); i++){
            Recipe.Ingredient ingredient = ingredients.get(i);
            ingredientsStr += " - " + ingredient.getIngredient()
                           + " : " + ingredient.getQuantityNormalized()
                           + " " + ingredient.getMeasure() + "\r\n";
        }
        mIngredients.setText(ingredientsStr);
    }

    /**
     * Responds to clicks from the list.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Helper.gotoRecipeStep(this, mRecipe, position);
        getSupportFragmentManager();
    }
}
