package com.example.android.bakingapp.providers;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;


@ContentProvider(
        authority = BakingProvider.AUTHORITY,
        database = BakingDatabase.class,
        packageName = "com.example.android.bakingapp")
public final class BakingProvider {

    public static final String AUTHORITY = "com.example.android.bakingapp";


    @TableEndpoint(table = BakingDatabase.INGREDIENT_TABLE)
    public static class INGREDIENTS_PROVIDER {

        @ContentUri(
                path = "ingredient",
                type = "vnd.android.cursor.dir/ingredient",
                defaultSort = BakingContract.IngredientContract.COLUMN_ID + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/ingredient");

        @InexactContentUri(
                path = "ingredient/*",
                name = "LIST_ID",
                type = "vnd.android.cursor.item/ingredient",
                whereColumn = BakingContract.IngredientContract.COLUMN_RECIPE_NAME,
                pathSegment = 1)
        public static Uri Recipe_Content_Uri(String recipeName) {
            return Uri.parse("content://" + AUTHORITY + "/ingredient/" + recipeName);
        }

    }


    @TableEndpoint(table = BakingDatabase.RECIPES_TABLE)
    public static class RECIPES_PROVIDER {

        @ContentUri(
                path = "recipe",
                type = "vnd.android.cursor.dir/recipe",
                defaultSort = BakingContract.RecipeContract.COLUMN_ID + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/recipe");

        @InexactContentUri(
                path = "recipe/#",
                name = "LIST_ID",
                type = "vnd.android.cursor.item/recipe",
                whereColumn = BakingContract.RecipeContract.RECIPE_ID,
                pathSegment = 1)
        public static Uri withId(int id) {
            return Uri.parse("content://" + AUTHORITY + "/recipe/" + id);
        }

    }


    @TableEndpoint(table = BakingDatabase.RECIPES_WIDGETS)
    public static class RECIPES_WIDGETS {

        @ContentUri(
                path = "widgets",
                type = "vnd.android.cursor.dir/widgets",
                defaultSort = BakingContract.RecipeWidgetsContract.WIDGET_ID + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/widgets");

        @InexactContentUri(
                path = "widgets/#",
                name = "LIST_ID",
                type = "vnd.android.cursor.item/widgets",
                whereColumn = BakingContract.RecipeWidgetsContract.WIDGET_ID,
                pathSegment = 1)
        public static Uri withId(int id) {
            return Uri.parse("content://" + AUTHORITY + "/widgets/" + id);
        }

    }
}