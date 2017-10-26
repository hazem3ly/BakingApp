package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.providers.BakingContract;
import com.example.android.bakingapp.providers.BakingProvider;

public class RecipesRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewFactory(this.getApplicationContext());
    }

    private class RemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        Cursor mCursor;

        RemoteViewFactory(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) mCursor.close();
            mCursor = mContext.getContentResolver().query(
                    BakingProvider.RECIPES_PROVIDER.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onDestroy() {
            if (mCursor != null)
                mCursor.close();
        }

        @Override
        public int getCount() {
            if (mCursor == null) return 0;
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (mCursor == null || mCursor.getCount() == 0) return null;
            mCursor.moveToPosition(position);
            int recipeIdInx = mCursor.getColumnIndex(BakingContract.RecipeContract.RECIPE_ID);
            int recipeNameInx = mCursor.getColumnIndex(BakingContract.RecipeContract.COLUMN_RECIPE_NAME);

            int recipeId = mCursor.getInt(recipeIdInx);
            String recipeName = mCursor.getString(recipeNameInx);

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipes_list_item);

            // Update the plant image
            views.setTextViewText(R.id.recipe_name, recipeName);


            // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
            Bundle extras = new Bundle();
            extras.putString(BakingContract.RecipeContract.COLUMN_RECIPE_NAME, recipeName);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.recipe_name, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
