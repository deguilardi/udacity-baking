package com.guilardi.baking;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.guilardi.baking.activities.StepsListActivity;
import com.guilardi.baking.data.Recipe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailTest {
    private static final Integer RECIPE_ID = 123;
    private static final String RECIPE_NAME = "Delicious recipe";
    private static final Integer RECIPE_SERVINGS = 8;
    private static final String RECIPE_IMAGE = "http://cdn-image.myrecipes.com/sites/default/files/styles/4_3_horizontal_-_1200x900/public/1506120378/MR_0917170472.jpg?itok=KPTNrvis";
    private static final float INGREDIENT_QTD_1= 1.0f;
    private static final String INGREDIENT_MEASURE_1 = "cup";
    private static final String INGREDIENT_NAME_1 = "flour";

    @Rule
    public IntentsTestRule<StepsListActivity> mActivityRule = new IntentsTestRule<StepsListActivity>(StepsListActivity.class){

        @Override
        protected Intent getActivityIntent(){
            List<Recipe.Ingredient> ingredients = new ArrayList<>();
            ingredients.add(new Recipe.Ingredient(INGREDIENT_QTD_1, INGREDIENT_MEASURE_1, INGREDIENT_NAME_1));
            List<Recipe.Step> steps = new ArrayList<>();
            steps.add(new Recipe.Step("short description","description"));
            Recipe recipe = new Recipe(RECIPE_ID, RECIPE_NAME, RECIPE_SERVINGS, RECIPE_IMAGE, ingredients, steps);

            Intent intent = new Intent();
            intent.putExtra(MyActivity.ARG_RECIPE, recipe);
            return intent;
        }
    };

    @Before
    public void stubAllExternalIntents(){
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void recipeDetailsAreShown(){
        // assert intent
        intending(hasComponent(hasShortClassName(".StepsListActivity"))).respondWith(new ActivityResult(Activity.RESULT_OK,null));

        // the toolbar has the recipe name
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar)))).check(matches(withText(RECIPE_NAME)));

        // the ingredients list match with ingredients values
        onView(withId(R.id.ingredients)).check(matches(withText(containsString(INGREDIENT_MEASURE_1))));
        onView(withId(R.id.ingredients)).check(matches(withText(containsString(INGREDIENT_NAME_1))));
    }
}
