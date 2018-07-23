package com.guilardi.baking.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.guilardi.baking.R;
import com.guilardi.baking.StepDetailFragment;
import com.guilardi.baking.activities.FullscreenVideoActivity;
import com.guilardi.baking.activities.StepDetailActivity;
import com.guilardi.baking.activities.StepsListActivity;
import com.guilardi.baking.data.Recipe;

/**
 * Created by deguilardi on 7/20/18.
 */

public final class Helper {
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isPhone(Context context){
        return !isTablet(context);
    }

    public static void gotoRecipeStep(Context context, Recipe recipe, int position){
        if(Helper.isPhone(context) || context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
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
        else{
            Bundle arguments = new Bundle();
            arguments.putParcelable(StepDetailFragment.ARG_RECIPE, recipe);
            arguments.putInt(StepDetailFragment.ARG_STEP_POSITION, position);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        }
    }
}
