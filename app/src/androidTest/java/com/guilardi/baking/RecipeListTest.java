package com.guilardi.baking;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.guilardi.baking.activities.RecipesListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeListTest {

    @Rule
    public ActivityTestRule<RecipesListActivity> mActivityRule = new ActivityTestRule<>(RecipesListActivity.class);

    @Test
    public void idlingResourceTest(){

        // click the first recipe of the list
        onView(withId(R.id.recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
    }
}
