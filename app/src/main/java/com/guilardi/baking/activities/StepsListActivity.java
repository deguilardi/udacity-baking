package com.guilardi.baking.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;

import com.guilardi.baking.custom.NonScrollListView;
import com.guilardi.baking.data.Recipe;

import com.guilardi.baking.R;

import java.util.List;

public class StepsListActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    public static final String ARG_ITEM = "recipe";

    private boolean mTwoPane;
    private Recipe mRecipe;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.step_detail_container) @Nullable NestedScrollView mStepDetailContainer;
    @BindView(R.id.steps_list) NonScrollListView mStepsList;
    @BindView(R.id.ingredients) TextView mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        // get the recipe
        Bundle b = getIntent().getExtras();
        if(b != null){
            mRecipe = (Recipe) b.get(ARG_ITEM);
            mToolbar.setTitle(mRecipe.getName());
        }
        else{
            Toast.makeText(this, "An error has occurred. Pleas try again later", Toast.LENGTH_LONG).show();
        }

        if (mStepDetailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
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
        Toast.makeText(this, "You have selected : " + mRecipe.getSteps().get(position).getShortDescription(), Toast.LENGTH_LONG).show();
    }
}
