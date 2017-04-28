package pollens.poupa.beaujean.com.pollens;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapsFragment extends Fragment implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    MapView mMapView;

    private static final String TAG = MapsFragment.class.getSimpleName();
    private GoogleMap mMap;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;

    // A default location (Paris, France) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(48.864716, 2.349014);
    private static final int DEFAULT_ZOOM = 5;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_LOCATION = "location";

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));

            RequestQueue mVolleyQueue = Volley.newRequestQueue(getActivity());
            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+mLastKnownLocation.getLatitude()+","+mLastKnownLocation.getLongitude()+"&sensor=true";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray arrayResult = jsonObject.getJSONArray("results");

                        JSONArray arrComponent = arrayResult.getJSONObject(0).getJSONArray("address_components");

                        String shortname = "";
                        String postal = "";

                        for (int i = 0; i < arrComponent.length(); i++) {
                            JSONArray arrType = arrComponent.getJSONObject(i).getJSONArray("types");
                            for (int j = 0; j < arrType.length(); j++) {

                                if (arrType.getString(j).equals("administrative_area_level_2") && !arrComponent.getJSONObject(i).getString("short_name").equals("")) {
                                    shortname = arrComponent.getJSONObject(i).getString("short_name");
                                }

                                if (arrType.getString(j).equals("postal_code") && !arrComponent.getJSONObject(i).getString("short_name").equals("")) {
                                    postal = arrComponent.getJSONObject(i).getString("short_name");
                                }
                            }
                        }

                        String miniCp = postal.substring(0,2);
                        Toast.makeText(getActivity(),
                                "Ville détectée: " + shortname + " (" + miniCp + ")",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            stringRequest.setShouldCache(false);
            stringRequest.setTag("Volley");
            mVolleyQueue.add(stringRequest);
        } catch (SecurityException e) {
            Log.d("info", "Could not locate user");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Demande de permission de localisation")
                        .setMessage("Pour vous localiser, veuillez accepter la demande de localisation")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        mMapView.getMapAsync(new OnMapReadyCallback() {

            /**
             * Manipulates the map when it's available.
             * This callback is triggered when the map is ready to be used.
             */
            @Override
            public void onMapReady(GoogleMap map) {
                mMap = map;

                // Lock zoom
                mMap.setMinZoomPreference(5);
                mMap.setMaxZoomPreference(10);

                // Show France globally
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 5));

                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getActivity().getApplicationContext(), R.raw.style_json));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }

                //Initialize Google Play Services
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Location Permission already granted
                        buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    } else {
                        //Request Location Permission
                        checkLocationPermission();
                    }
                }
                else {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }

                // Use a custom info window adapter to handle multiple lines of text in the
                // info window contents.
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    // Return null here, so that getInfoContents() is called next.
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        // Inflate the layouts for the info window, title and snippet.
                        View infoWindow = getActivity().getLayoutInflater().inflate(R.layout.custom_info_contents,
                                (FrameLayout)getView().findViewById(R.id.map), false);

                        TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                        title.setText(marker.getTitle());

                        TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                        snippet.setText(marker.getSnippet());

                        return infoWindow;
                    }
                });

                // Load departments
                new MyAsyncTask().execute();
            }

            class MyAsyncTask extends AsyncTask<Void, Void, Void> {

                ProgressDialog pd;
                GeoJsonLayer geoJsonLayer;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pd = new ProgressDialog(getActivity());
                    pd.setMessage("Chargement de la carte");
                    pd.show();
                }

                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        FeedDB feedDB = new FeedDB(getActivity());
                        feedDB.loadDepartments();

                        geoJsonLayer = new GeoJsonLayer(mMap, R.raw.departements, getActivity().getApplicationContext());

                        for (GeoJsonFeature feature : geoJsonLayer.getFeatures()) {
                            GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();

                            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());

                            Cursor cursor = databaseHelper.getDepartment(feature.getProperty("code"));

                            String color = "white";

                            if(cursor != null && cursor.moveToFirst()) {
                                color = cursor.getString(4);
                            }

                            switch (color) {
                                case "yellow":
                                    style.setFillColor(Color.YELLOW);
                                    break;
                                case "green-1":
                                    style.setFillColor(Color.GREEN);
                                    break;
                                case "green-2":
                                    style.setFillColor(0xFF00AA00);
                                    break;
                                case "red":
                                    style.setFillColor(Color.RED);
                                    break;
                                case "orange":
                                    style.setFillColor(0xFFFFA500);
                                    break;
                                default:
                                    style.setFillColor(Color.WHITE);
                                    break;
                            }

                            style.setStrokeColor(Color.BLACK);
                            feature.setPolygonStyle(style);
                        }

                    } catch (Exception e) {
                        Log.d("Error is : ", e.toString());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    pd.cancel();
                    geoJsonLayer.addLayerToMap();

                    // Demonstrate receiving features via GeoJsonLayer clicks.
                    geoJsonLayer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
                        @Override
                        public void onFeatureClick(Feature feature) {
                            Intent intent = new Intent(getActivity(), PollensDepartmentActivity.class);
                            intent.putExtra("department", feature.getProperty("nom"));
                            intent.putExtra("code", feature.getProperty("code"));
                            startActivity(intent);
                        }

                    });
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Nous n'avons pas pu vous localiser", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
