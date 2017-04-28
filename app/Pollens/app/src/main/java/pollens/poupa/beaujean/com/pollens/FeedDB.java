package pollens.poupa.beaujean.com.pollens;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Populate database with data from our own API
 * We use RequestFuture from Volley to get synchronous requests
 */

public class FeedDB {

    private DBHelper dbHelper;
    private Context context;
    private SimpleDateFormat sdf;
    private String today;
    private SharedPreferences pref;

    /**
     * Load some date
     * @param context contect
     */
    public FeedDB(final Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        dbHelper.getWritableDatabase();

        sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        today = sdf.format(new Date());
        pref = context.getApplicationContext().getSharedPreferences("lastcheck", android.content.Context.MODE_PRIVATE);
    }

    /**
     * Load all departments
     * Insert into SQLite
     */
    public void loadDepartments() {
        String lastCheck = pref.getString("lastcheck", null);

        if (lastCheck == null || !lastCheck.equals(today)) {
            RequestQueue mVolleyQueue = Volley.newRequestQueue(context.getApplicationContext());
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            String url = "http://pollens.poupa.fr/api/department";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
            mVolleyQueue.add(request);

            try {
                dbHelper.deleteDepartments();
                JSONObject jsonObject = future.get();
                try {
                    JSONArray departments = jsonObject.getJSONArray("departments");

                    for (int i = 0; i < departments.length(); i++) {
                        JSONObject row = departments.getJSONObject(i);

                        String name = row.getString("name");
                        String number = row.getString("number");
                        String risk = row.getString("risk");
                        String color = row.getString("color");

                        dbHelper.insertDepartment(name, number, Integer.parseInt(risk), color);
                    }

                    dbHelper.close();

                    // Store last check
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("lastcheck");
                    editor.putString("lastcheck", today);
                    editor.apply();
                } catch (JSONException e) {
                    Log.i("Error", e.toString());
                }

            } catch (InterruptedException | ExecutionException e) {
                Log.i("Error", e.toString());
            }
        }
    }

    /**
     * Load the risks for a specified department
     * @param number department ID
     */
    public void loadRisk(final String number) {
        String lastCheck = pref.getString("lastcheck"+number, null);

        if (lastCheck == null || !lastCheck.equals(today)) {
            RequestQueue mVolleyQueue = Volley.newRequestQueue(context.getApplicationContext());
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            String url = "http://pollens.poupa.fr/api/department/"+number;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
            mVolleyQueue.add(request);

            try {
                dbHelper.deleteRisk(number);
                JSONObject jsonObject = future.get();
                try {
                    JSONArray departments = jsonObject.getJSONArray("risks");

                    for (int i = 0; i < departments.length(); i++) {
                        JSONObject row = departments.getJSONObject(i);

                        String name = row.getString("name");
                        int risk = row.getInt("risk");

                        dbHelper.insertRisk(name, number, risk);
                    }

                    dbHelper.close();

                    // Store last check
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("lastcheck"+number);
                    editor.putString("lastcheck"+number, today);
                    editor.apply();
                } catch (JSONException e) {
                    Log.i("Error", e.toString());
                }

            } catch (InterruptedException | ExecutionException e) {
                Log.i("Error", e.toString());
            }
        }
    }
}
