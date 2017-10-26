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

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = BakingDatabase.VERSION, packageName = "com.example.android.bakingapp")
public class BakingDatabase {

    public static final int VERSION = 12;

    @Table(BakingContract.RecipeContract.class)
    public static final String RECIPES_TABLE = "recipes_table";

    @Table(BakingContract.IngredientContract.class)
    public static final String INGREDIENT_TABLE = "ingredient_table";

   @Table(BakingContract.RecipeWidgetsContract.class)
    public static final String RECIPES_WIDGETS = "widgets_table";

}
