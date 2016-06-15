package comicaday.lunarmonk.com.comicaday;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Created by madhur on 14/06/16.
 */
public class BootAndAlarmReceiver extends BroadcastReceiver {

    public static String AlarmAction = "Comic_Alarm_Action";
    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;
    private static int ID_UNIQUE = 93847;
    private static int ID_ALARM_UNIQUE = 457349;

    private String COMIC_PREFS = "comicprefs";
    String ID_COMIC_URL = "COMIC_URL_ID";
    String ID_TIME = "time";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            setAlarm(context);
        }

        if (intent.getAction().equals(AlarmAction)) {
            launchNotification(context);
        }
    }

    private void launchNotification(Context context)
    {
        updateComicId(context);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Comics")
                        .setContentText("Your daily Comics is here!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, ComicDisplayActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ComicDisplayActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setAutoCancel(true);
        Notification notification = mBuilder.build();

        mNotificationManager.notify(ID_UNIQUE, notification);
    }

    private void updateComicId(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(COMIC_PREFS, Context.MODE_PRIVATE);
        String str = sharedpreferences.getString(ID_COMIC_URL, null);
        int id = Integer.parseInt(str);
        id++;
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(ID_COMIC_URL, Integer.toString(id));
        editor.commit();
    }

    public static void setAlarm(Context context)
    {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BootAndAlarmReceiver.class);
        intent.setAction(BootAndAlarmReceiver.AlarmAction);
        alarmIntent = PendingIntent.getBroadcast(context, ID_ALARM_UNIQUE, intent, PendingIntent.FLAG_NO_CREATE);

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES/3,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES/3, alarmIntent);





    }
}