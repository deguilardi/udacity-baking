package com.guilardi.baking.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.guilardi.baking.MyWidgetProvider;
import com.guilardi.baking.RecipesAdapter;
import com.guilardi.baking.MyActivity;
import com.guilardi.baking.data.Recipe;
import com.guilardi.baking.utilities.ExoPlayerVideoHandler;
import com.guilardi.baking.utilities.Helper;
import com.guilardi.baking.utilities.NetworkUtils;

import com.guilardi.baking.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class RecipesListActivity extends MyActivity implements RecipesAdapter.RecipesAdapterOnClickHandler {

    private RecipesAdapter mRecipesAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private boolean mHasSelection;

    @BindView(R.id.main_content) CoordinatorLayout mMainContent;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.recipes_list) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());

        // force landscape on tablets
        if(Helper.isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        setupRecyclerView();
        loadRecipesData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHasSelection = false;
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
    }

    private void setupRecyclerView() {
        assert mRecyclerView != null;
        int numColumns = Helper.isTablet(this) ? 3 : 1;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numColumns);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecipesAdapter = new RecipesAdapter(this, this);
        mRecyclerView.setAdapter(mRecipesAdapter);
    }

    private void loadRecipesData(){
        final Context context = this;
        Callback<List<Recipe>> callback = new Callback<List<Recipe>>(){

            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @Nullable Response<List<Recipe>> response) {
                if(response == null){
                    showError();
                    return;
                }
                List<Recipe> recipes = response.body();
                mRecipesAdapter.swapData(recipes);
                if (mPosition == RecyclerView.NO_POSITION){
                    mPosition = 0;
                }
                if (recipes != null && recipes.size() != 0) {
                    showRecipesDataView();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @Nullable Throwable t) {
                showError();
            }

            private void showError(){
                Snackbar snackbar = Snackbar
                        .make(mMainContent, R.string.no_internet, Snackbar.LENGTH_LONG)
                        .setAction(R.string.try_again, new View.OnClickListener() {
                            @Override
                            public void onClick(View snackbar2View) {
                                loadRecipesData();
                            }
                        });
                snackbar.show();
                Toast.makeText(context, "Something went wrong, please check your internet connection and try again!", Toast.LENGTH_LONG).show();
            }
        };
        NetworkUtils.getInstance().loadRecipes(callback);
        showLoading();
    }

    /**
     * Show the loading spinning image
     */
    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * Show the list content and hide the loading
     */
    private void showRecipesDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

        // scroll to the right position
        // delays to wait for the elements to be on the screen
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mPosition);
            }}, 100);
    }

    /**
     * Responds to clicks from the list.
     *
     * @param position The position of the selected item
     */
    @Override
    public void onClick(int position, RecipesAdapter.RecipesAdapterViewHolder adapterViewHolder) {
        if(mHasSelection){
            return;
        }
        mHasSelection = true;
        Recipe selectedRecipe = mRecipesAdapter.getData().get(position);

        Intent detailsActivityIntent = new Intent(RecipesListActivity.this, StepsListActivity.class);
        detailsActivityIntent.putExtra(StepsListActivity.ARG_RECIPE, selectedRecipe);
        startActivity(detailsActivityIntent);

        // save the selected recipe to show on widgets
        SharedPreferences prefs = getSharedPreferences(MyWidgetProvider.KEY_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson recipeGson = new Gson();
        String recipeJson = recipeGson.toJson(selectedRecipe);
        prefsEditor.putString(MyWidgetProvider.ARG_RECIPE,recipeJson);
        prefsEditor.apply();

        // update widgets immediatly
        Intent intent = new Intent(this, MyWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        ComponentName component = new ComponentName(getApplication(), MyWidgetProvider.class);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(component);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

}
