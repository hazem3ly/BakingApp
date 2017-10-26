package com.example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.providers.BakingContract;
import com.example.android.bakingapp.providers.BakingProvider;

import static com.example.android.bakingapp.providers.BakingContract.RecipeWidgetsContract.WIDGET_ID;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RecipesService.CheckWidgetSaved(context, appWidgetId);
    }

    public static void updateIngredientWidgets(Context context, String selectedRecipe,
                                               AppWidgetManager appWidgetManager, int widgetId) {

        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.ingredient_list_items);

        RemoteViews views = getIngredientList(context, selectedRecipe);

        appWidgetManager.updateAppWidget(widgetId, views);
    }

    public static void updateRecipesWidgets(Context context, AppWidgetManager appWidgetManager,
                                            int recipeId) {
        RemoteViews views = getRecipesList(context, recipeId);
        appWidgetManager.updateAppWidget(recipeId, views);

    }

    private static RemoteViews getRecipesList(Context context, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        Intent intent = new Intent(context, RecipesRemoteViewsService.class);
        views.setRemoteAdapter(R.id.recipes_list_items, intent);
        views.setTextViewText(R.id.recipe_name, "Select Recipe");

        Intent recipeClicked = new Intent(context, RecipesService.class);
        recipeClicked.setAction(RecipesService.ACTION_INGREDIENT_LIST);
        recipeClicked.putExtra(RecipesService.EXTRA_RECIPE_WIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, recipeClicked,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.recipes_list_items, pendingIntent);

        return views;
    }

    private static RemoteViews getIngredientList(Context context, String selectedRecipe) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, IngredientRemoteViewsService.class);
        intent.putExtra(BakingContract.IngredientContract.COLUMN_RECIPE_NAME, selectedRecipe);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.ingredient_list_items, intent);
        views.setTextViewText(R.id.recipe_name, selectedRecipe);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        if (appWidgetIds[0] != -1)
           context.getContentResolver().delete(BakingProvider.RECIPES_WIDGETS.CONTENT_URI,
                    WIDGET_ID + "=?", new String[]{String.valueOf(appWidgetIds[0])});
        super.onDeleted(context, appWidgetIds);
    }
}

