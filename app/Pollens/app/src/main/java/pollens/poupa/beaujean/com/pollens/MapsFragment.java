package pollens.poupa.beaujean.com.pollens;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
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


public class MapsFragment extends Fragment {
    MapView mMapView;

    private static final String TAG = MapsFragment.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(48.864716, 2.349014);
    private static final int DEFAULT_ZOOM = 10;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

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

                // Turn on the My Location layer and the related control on the map.
                updateLocationUI();

                // Get the current location of the device and set the position of the map.
                getDeviceLocation();

                // Load departments
                new MyAsyncTask().execute();
            }

            /**
             * Gets the current location of the device, and positions the map's camera.
             */
            private void getDeviceLocation() {
                /*
                 * Request location permission, so that we can get the location of the
                 * device. The result of the permission request is handled by a callback,
                 * onRequestPermissionsResult.
                 */
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
                /*
                 * Get the best and most recent location of the device, which may be null in rare
                 * cases when a location is not available.
                 */
                if (mLocationPermissionGranted) {
                    mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                }

                // Set the map's camera position to the current location of the device.
                if (mCameraPosition != null) {
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
                } else if (mLastKnownLocation != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));


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
                                        "Résultat: " + shortname + " (" + miniCp + ")",
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


                } else {
                    Log.d(TAG, "Current location is null. Using defaults.");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            }

            /**
             * Handles the result of the request for location permissions.
             */
            public void onRequestPermissionsResult(int requestCode,
                                                   @NonNull String permissions[],
                                                   @NonNull int[] grantResults) {
                mLocationPermissionGranted = false;
                switch (requestCode) {
                    case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                        // If request is cancelled, the result arrays are empty.
                        if (grantResults.length > 0
                                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = true;
                        }
                    }
                }
                updateLocationUI();
            }

            /**
             * Updates the map's UI settings based on whether the user has granted location permission.
             */
            private void updateLocationUI() {
                if (mMap == null) {
                    return;
                }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }

                if (mLocationPermissionGranted) {
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                } else {
                    mMap.setMyLocationEnabled(false);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mLastKnownLocation = null;
                }
            }

            class MyAsyncTask extends AsyncTask<Void, Void, Void> {

                ProgressDialog pd;
                GeoJsonLayer layer;

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
                        layer = new GeoJsonLayer(mMap, R.raw.departements, getActivity().getApplicationContext());

                        for (GeoJsonFeature feature : layer.getFeatures()) {
                            GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                            style.setFillColor(Color.RED);
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
                    layer.addLayerToMap();

                    // Demonstrate receiving features via GeoJsonLayer clicks.
                    layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
                        @Override
                        public void onFeatureClick(Feature feature) {
                            /*Toast.makeText(getActivity(),
                                    "Département: " + feature.getProperty("nom") + " (" + feature.getProperty("code") + ")",
                                    Toast.LENGTH_SHORT).show();*/

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
}
