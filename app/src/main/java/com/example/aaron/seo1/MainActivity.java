package com.example.aaron.seo1;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import android.os.Environment;
import android.app.AlertDialog;
import android.os.AsyncTask;




import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;


import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity implements LocationListener {
    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        org.osmdroid.config.Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);
        mv.getController().setCenter(new GeoPoint(50.91, -1.396));

        download t = new download();
        t.execute();

        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {

            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getTitle() + item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }


            public boolean onItemLongPress(int index, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };


    }

    // GPS Functions
    public void onLocationChanged(Location newLoc) {
        mv.getController().setCenter(new GeoPoint(newLoc));
    }

    public void onProviderDisabled(String provider) {
        // Toast.makeText(this, "Provider" + provider + "disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider) {
        //  Toast.makeText(this, "Provider" + provider + "enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        //  Toast.makeText(this, "Status changed:" + status, Toast.LENGTH_LONG).show();
    }

    // Item Menu Function
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addrestaurant) {
            Intent intent = new Intent(this, RestaurantActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (item.getItemId() == R.id.preferences) {
            Intent intent = new Intent(this, PrefActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (item.getItemId() == R.id.load) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/addedRestaurants.csv"));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] comp = line.split(",");
                    if (comp.length == 4) {
                        Double lat = Double.valueOf(comp[2]).doubleValue();
                        Double lon = Double.valueOf(comp[3]).doubleValue();
                        OverlayItem itm = new OverlayItem(comp[0], comp[1], new GeoPoint(lat, lon));
                        try {
                            items.addItem(itm);
                        } catch (Exception e) {
                        }
                    }
                }
            } catch (IOException e) {
            }


            return true;

        }
        if (item.getItemId() == R.id.save) {// if pref is true
            {
                try {
                    PrintWriter printwriter =
                            new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/addedRestaurants.csv", true));

                    for (int i = 0; i < items.size(); i++) //add each marker to file
                    {
                        OverlayItem resValues = items.getItem(i);
                        printwriter.println(resValues.getTitle() + "," + resValues.getSnippet() + "," + resValues.getPoint().getLatitude()
                                + "," + resValues.getPoint().getLongitude());
                    }
                    printwriter.close();
                } catch (IOException e) {
                    System.out.println("Failed to add restaurants");
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String strRestaurantName = extras.getString("com.example.RestaurantName");
                String strRestaurantAddress = extras.getString("com.example.RestaurantAddress");
                String strRestaurantCuisine = extras.getString("com.example.RestaurantCuisine");
                String strRestaurantRating = extras.getString("com.example.RestaurantRating");
                items = new ItemizedIconOverlay<>(this, new ArrayList<OverlayItem>(), markerGestureListener);
                OverlayItem mapMarker = new OverlayItem(strRestaurantName,
                        "The restaurant address is:" + strRestaurantAddress +
                                "The restaurant cuisine is:" + strRestaurantCuisine +
                                "The restaurant rating is:" + strRestaurantRating, mv.getMapCenter());
                items.addItem(mapMarker);
                mv.getOverlays().add(items);

                // code to save to file
            }


        }
    }

    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean autosave = prefs.getBoolean("autosave", true);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/addedRestaurants.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] comp = line.split(",");
                if (comp.length == 4) {
                    Double lat = Double.valueOf(comp[2]).doubleValue();
                    Double lon = Double.valueOf(comp[3]).doubleValue();
                    OverlayItem itm = new OverlayItem(comp[0], comp[1], new GeoPoint(lat, lon));
                    try {
                        items.addItem(itm);
                    } catch (Exception e) {
                    }


                }
            }
        } catch (IOException e) {
        }


        if (items != null) {
            if (autosave) {
                try {
                    PrintWriter printwriter =
                            new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/addedRestaurants.csv", true));

                    for (int i = 0; i < items.size(); i++) //add each marker to file
                    {
                        OverlayItem resValues = items.getItem(i);
                        printwriter.println(resValues.getTitle() + "," + resValues.getSnippet() + "," + resValues.getPoint().getLatitude()
                                + "," + resValues.getPoint().getLongitude());
                    }
                    printwriter.close();
                } catch (IOException e) {
                    System.out.println("Failed to add restaurants");
                }
            }

        }


    }

    class download extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... unused) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://www.free-map.org.uk/course/mad/ws/get.php?year=18&username=user002&format=csv");
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();

                if (conn.getResponseCode() == 200)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String result = "", line;
                    while ((line = br.readLine()) != null) {

                        String[] comp = line.split(",");
                        if (comp.length == 6) {
                            Double lat = Double.valueOf(comp[5]).doubleValue();
                            Double lon = Double.valueOf(comp[4]).doubleValue();
                            OverlayItem itm = new OverlayItem(comp[0], comp[1], new GeoPoint(lat, lon));

                                //items.addItem(itm);


                            }


                        result += line;



                    }
                    return result;
                }
                else

                    {
                    return "HTTP ERROR: " + conn.getResponseCode();
                    }
            }
            catch (IOException e)
            {
                return e.toString();
            }
            finally
            {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        public void onPostExecute(String result) {
            mv.getOverlays().add(items);
        }

    }
}
