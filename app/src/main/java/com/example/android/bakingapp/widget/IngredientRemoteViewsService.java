package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.providers.BakingContract;
import com.example.android.bakingapp.providers.BakingProvider;


public class IngredientRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewFactory(this.getApplicationContext(),
                intent.getStringExtra(BakingContract.IngredientContract.COLUMN_RECIPE_NAME));
    }

    private class RemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        Cursor mCursor;
        private String recipeName;

        RemoteViewFactory(Context mContext, String recipeName) {
            this.mContext = mContext;
            this.recipeName = recipeName;
            Log.d("RECIVED_RECIPE",recipeName);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) mCursor.close();
            mCursor = mContext.getContentResolver().query(
                    BakingProvider.INGREDIENTS_PROVIDER.Recipe_Content_Uri(recipeName),
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
            int MEASUREInx = mCursor.getColumnIndex(BakingContract.IngredientContract.MEASURE);
            int QUANTITYInx = mCursor.getColumnIndex(BakingContract.IngredientContract.QUANTITY);
            int INGREDIENTInx = mCursor.getColumnIndex(BakingContract.IngredientContract.INGREDIENT);

            String MEASURE = mCursor.getString(MEASUREInx);
            String QUANTITY = mCursor.getString(QUANTITYInx);
            String INGREDIENT = mCursor.getString(INGREDIENTInx);

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingrident_list_item);

            views.setTextViewText(R.id.measure_text, MEASURE);
            views.setTextViewText(R.id.quantity_text, QUANTITY);
            views.setTextViewText(R.id.ingredient_text, INGREDIENT);


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
