package com.guilardi.baking.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.guilardi.baking.activities.FullscreenVideoActivity;
import com.guilardi.baking.activities.StepDetailActivity;
import com.guilardi.baking.activities.StepsListActivity;
import com.guilardi.baking.data.Recipe;

/**
 * Created by deguilardi on 7/20/18.
 */

public final class Helper {
    public static boolean isTablet( Context context ) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void gotoRecipeStep(Context context, Recipe recipe, int position){
        Recipe.Step step = recipe.getSteps().get(position);
        Intent intent;
        if( context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                || step.getVideoURL().equals("")) {
            intent = new Intent(context, StepDetailActivity.class);
        }
        else{
            intent = new Intent(context, FullscreenVideoActivity.class);
            intent.putExtra(FullscreenVideoActivity.ARG_URL, step.getVideoURL());
        }
        intent.putExtra(StepDetailActivity.ARG_RECIPE, recipe);
        intent.putExtra(StepDetailActivity.ARG_STEP_POSITION, position);
        context.startActivity(intent);
    }
}
