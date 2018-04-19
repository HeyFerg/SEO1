package com.example.aaron.seo1;


import android.content.Intent;
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


//import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity implements LocationListener
{
    MapView mv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        org.osmdroid.config.Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager mgr=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

        mv = (MapView)findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);

    }

    public void onLocationChanged(Location newLoc)
    {
        mv.getController().setCenter(new GeoPoint(newLoc));
    }

    public void onProviderDisabled(String provider)
    {
        //Toast.makeText(this, "Provider" + provider + "disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider)
    {
        //Toast.makeText(this, "Provider" + provider + "enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        //Toast.makeText(this, "Status changed:" + status, Toast.LENGTH_LONG).show();
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.addrestaurant)
        {
            Intent intent = new Intent(this, RestaurantActivity.class);
            startActivityForResult(intent, 0 );
            return true;
        }
        if(item.getItemId() == R.id.preferences)
        {
            Intent intent = new Intent(this, PrefActivity.class);
            startActivityForResult(intent, 0 );
            return true;
        }
        if(item.getItemId() == R.id.load)
        {

            return true;
        }
        if(item.getItemId() == R.id.save)
        {

            return true;
        }
        return false;
    }
}
