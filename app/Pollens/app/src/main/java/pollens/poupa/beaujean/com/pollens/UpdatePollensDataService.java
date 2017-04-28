package pollens.poupa.beaujean.com.pollens;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

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
        // I want to restart this service again in one hour
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + (1000 * 60 * 60 * 24),
                PendingIntent.getService(this, 0, new Intent(this, UpdatePollensDataService.class), 0)
        );
    }
}
