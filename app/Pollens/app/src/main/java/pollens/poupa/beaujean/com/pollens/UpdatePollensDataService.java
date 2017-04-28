package pollens.poupa.beaujean.com.pollens;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Update Pollen Data as a Service
 */
public class UpdatePollensDataService extends IntentService {
    public UpdatePollensDataService() {
        super("UpdatePollensDataService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        FeedDB feedDB = new FeedDB(this);
        feedDB.loadDepartments();
    }

    @Override
    public void onDestroy() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int syncFrequency = prefs.getInt("sync_frequency", -1);

        if (syncFrequency != -1) {
            // Restart service after the time selected
            AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarm.set(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + (1000 * 60 * syncFrequency),
                    PendingIntent.getService(this, 0, new Intent(this, UpdatePollensDataService.class), 0)
            );
        }
    }
}
