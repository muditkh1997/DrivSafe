package mudit.com.drivsafe;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

/**
 * Created by admin on 11/28/2017.
 */

public class SimpleWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        DatabaseReference mdatabase=FirebaseDatabase.getInstance().getReference();

        final int count = appWidgetIds.length;
        final String[] pressure = new String[1];
        final String[] temp = new String[1];
        Log.d("My", "onUpdate: ");
        mdatabase.child("Readings").limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                pressure[0] =dataSnapshot.child("pressure").getValue().toString();
                temp[0] =dataSnapshot.child("temperature").getValue().toString();
                for (int i = 0; i < count; i++) {
                    int widgetId = appWidgetIds[i];
                    String number = String.format("%03d", (new Random().nextInt(900) + 100));

                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                            R.layout.simple_widget);
                    ComponentName thisWidget = new ComponentName( context, SimpleWidgetProvider.class );

                    remoteViews.setTextViewText(R.id.textView,pressure[0] );
                    remoteViews.setTextViewText(R.id.textView1,temp[0] );

                    AppWidgetManager.getInstance( context ).updateAppWidget( thisWidget, remoteViews );

                    Intent intent = new Intent(context, SimpleWidgetProvider.class);

                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
//                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
