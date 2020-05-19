package com.example.xvso.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.xvso.R;


class XvsOWidgetService extends RemoteViewsService {

    public static final String LOG_TAG = "RecipeWidgetService";

    /**
     * @param intent
     * @return RemoteViewsFactory objects
     * RemoteViewFactory class  = adapter
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeWidgetItemFactory(getApplicationContext(), intent);
    }

    /**
     * gets the data from our data source
     */
    class RecipeWidgetItemFactory implements RemoteViewsFactory {

        // context
        private Context mContext;
        // to distinguish between widget versions
        private int mAppWidgetId;

        private int player1Score;
        private int player2Score;

        RecipeWidgetItemFactory(Context context, Intent intent) {
            mContext = context;
            this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        /**
         * will be triggered when we will first instantiate the RecipeWidgetItemFactory
         * connect to data source
         */
        @Override
        public void onCreate() {
        }

        /**
         * updates the widget
         */
        @Override
        public void onDataSetChanged() {
            getData();
        }

        /**
         * closes connection to the data source
         */
        @Override
        public void onDestroy() {

        }

        /**
         * @return the number of items that you want to display in the list
         */
        @Override
        public int getCount() {
            return 0;
        }

        /**
         * loads data from the data source into the item view of the list view
         *
         * @param position
         * @return RemoteViews objects
         */
        @Override
        public RemoteViews getViewAt(int position) {
            // creates a RemoteViews object
            // each entry in a ListView = one RemoteViews object
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.xvso_widget);

            return views;
        }

        /**
         * triggered when the data is loading
         *
         * @return
         */
        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        /**
         * @return the number of different types of views of the collection
         */
        @Override
        public int getViewTypeCount() {
            return 1;
        }

        /**
         * what Id to return for each object
         *
         * @param position
         * @return position (the id and the position is the same)
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * @return true, if the id for each item stays the same
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }

        public int getData(){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            player1Score = sharedPreferences.getInt(Xvs0WidgetPreferences.SCORE_PLAYER1, player1Score);
            player2Score = sharedPreferences.getInt(Xvs0WidgetPreferences.SCORE_PLAYER2, player2Score);
            return player1Score;
        }
    }
}

