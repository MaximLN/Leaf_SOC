package com.example.leaf_soc;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class LeafWidget extends AppWidgetProvider {
    static String widgetTempText = "NaN";
    static String widgetSocText = "NaN";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.leaf_widget);
        views.setTextViewText(R.id.appwidgetSocText, widgetSocText);
        views.setTextViewTextSize(R.id.appwidgetSocText, TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/20);

        views.setTextViewText(R.id.appwidgetTempText, widgetTempText);
        views.setTextViewTextSize(R.id.appwidgetTempText, TypedValue.COMPLEX_UNIT_PX, MainActivity.DISPLAY_HEIGHT/20);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        startTimer(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    Timer myTimer = new Timer();
    public void startTimer (final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        myTimer.schedule(new TimerTask() {
            @Override
                    public void run() {
                updateAppWidget(context, appWidgetManager, appWidgetIds[0]);
            }
        }, 0, 10000);
    }
}

