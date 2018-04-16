package com.example.aaron.seo1;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

//import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity
{
    MapView mv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        org.osmdroid.config.Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mv = (MapView)findViewById(R.id.map1);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemsSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.addrestaurant)
        {
            Intent intent = new Intent(this, RestaurauntActivity.class);
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
    }
}
