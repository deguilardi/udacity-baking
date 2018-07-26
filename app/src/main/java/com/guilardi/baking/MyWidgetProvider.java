package com.guilardi.baking;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.guilardi.baking.data.Recipe;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyWidgetProvider extends AppWidgetProvider{

    public static String KEY_PREFERENCES = "widgetPreferences";
    public static String ARG_RECIPE = "widgetRecipe";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // retrieve the last seen recipe
            SharedPreferences prefs = context.getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE);
            Gson gson = new Gson();
            String json = prefs.getString(ARG_RECIPE, "");
            Recipe recipe = gson.fromJson(json, Recipe.class);

            // Set the ingredients text
            if(recipe != null){
                StringBuilder ingredientsStr = new StringBuilder();
                List<Recipe.Ingredient> ingredients = recipe.getIngredients();
                for(int i = 0; i < ingredients.size(); i++){
                    Recipe.Ingredient ingredient = ingredients.get(i);
                    ingredientsStr.append(" - ").append(ingredient.getIngredient()).append(" : ").append(ingredient.getQuantityNormalized()).append(" ").append(ingredient.getMeasure()).append("\r\n");
                }
                remoteViews.setTextViewText(R.id.ingredients, ingredientsStr);
            }
            else {
                remoteViews.setTextViewText(R.id.ingredients, context.getString(R.string.widget_default_message));
            }

            // update the widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
