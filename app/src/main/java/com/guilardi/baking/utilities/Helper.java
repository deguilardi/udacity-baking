package com.guilardi.baking.utilities;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by deguilardi on 7/20/18.
 */

public final class Helper {
    public static boolean isTablet( Context context ) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
