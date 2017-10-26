package com.example.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bakingapp.providers.BakingContract;
import com.example.android.bakingapp.providers.BakingProvider;

import static com.example.android.bakingapp.providers.BakingContract.RecipeWidgetsContract.RECIPE_NAME;
import static com.example.android.bakingapp.providers.BakingContract.RecipeWidgetsContract.WIDGET_ID;


public class RecipesService extends IntentService {


    public static final String ACTION_INGREDIENT_LIST = "com.example.android.bakingapp.widget.ingredient";
    public static final String EXTRA_RECIPE_WIDGET_ID = "com.example.android.bakingapp.widget.recipe_id";
    public static final String ACTION_CHECK_WIDGT = "com.example.android.bakingapp.widget.checkwidget";


    public RecipesService() {
        super("RecipesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INGREDIENT_LIST.equals(action)) {
                int widgetId = intent.getIntExtra(EXTRA_RECIPE_WIDGET_ID, -1);
                String selectedRecipe = intent.getExtras().getString(BakingContract.RecipeContract.COLUMN_RECIPE_NAME);
                Log.d("id", String.valueOf(selectedRecipe));
                insertWidgetDatabase(selectedRecipe,widgetId);
                updateWidgetWithIngredients(selectedRecipe, widgetId);
            }else if (ACTION_CHECK_WIDGT.equals(action)){
                int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                String recipe_name = widgetIdInDatabase(widgetId);
                Log.d("ingredient recipe name",String.valueOf(recipe_name));
                if (recipe_name == null)updateWidgetWithRecipes(widgetId);
                else updateWidgetWithIngredients(recipe_name,widgetId);

            }
        }
    }

    private void insertWidgetDatabase(String selectedRecipe, int widgetId) {
        if (widgetId!=-1 && selectedRecipe!=null){
            ContentValues cv = new ContentValues();
            cv.put(WIDGET_ID,widgetId);
            cv.put(RECIPE_NAME,selectedRecipe);
            getContentResolver().insert(BakingProvider.RECIPES_WIDGETS.CONTENT_URI,cv);
        }
    }

    private String widgetIdInDatabase(int widgetId) {

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) return null;

        Cursor cursor = getContentResolver().query(BakingProvider.RECIPES_WIDGETS.withId(widgetId),null,null,null,null);
        if (cursor!=null){
            if (cursor.getCount()>0){
                cursor.moveToFirst();
                String  recipe_name = cursor.getString(cursor.getColumnIndex(RECIPE_NAME));
                cursor.close();
                return recipe_name;
            }
            cursor.close();
        }

        return null;
    }


    private void updateWidgetWithRecipes(int widgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
            RecipeWidgetProvider.updateRecipesWidgets(this, appWidgetManager, widgetId);
    }

    private void updateWidgetWithIngredients(String selectedRecipe, int widgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID){
            RecipeWidgetProvider.updateIngredientWidgets(this, selectedRecipe, appWidgetManager, widgetId);
        }

    }

    public static void CheckWidgetSaved(Context context, int appWidgetId) {
        Intent intent = new Intent(context, RecipesService.class);
        intent.setAction(ACTION_CHECK_WIDGT);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        context.startService(intent);
    }
}
