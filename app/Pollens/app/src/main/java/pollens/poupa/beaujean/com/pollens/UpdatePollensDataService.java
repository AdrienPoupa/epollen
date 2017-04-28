package pollens.poupa.beaujean.com.pollens;

import android.app.IntentService;
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
    }

}
