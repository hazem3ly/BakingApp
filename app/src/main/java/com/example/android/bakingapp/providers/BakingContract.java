/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.example.android.bakingapp.providers;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) library to define the columns in a
 * content provider baked by a database
 */

public class BakingContract {


    public interface RecipeContract {
        @DataType(DataType.Type.INTEGER)
        @PrimaryKey(onConflict = ConflictResolutionType.IGNORE)
        @AutoIncrement
        public static final String COLUMN_ID = "_id";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String RECIPE_ID = "recipe_id";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
    }

    public interface IngredientContract {
        @DataType(DataType.Type.INTEGER)
        @PrimaryKey(onConflict = ConflictResolutionType.IGNORE)
        @AutoIncrement
        public static final String COLUMN_ID = "_id";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_RECIPE_NAME = "recipe_name";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String QUANTITY = "quantity";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String MEASURE = "measure";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String INGREDIENT = "ingredient";
    }

    public interface RecipeWidgetsContract {

        @DataType(DataType.Type.INTEGER)
        @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
        public static final String WIDGET_ID = "widget_id";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String RECIPE_NAME = "recipe_name";
    }

}