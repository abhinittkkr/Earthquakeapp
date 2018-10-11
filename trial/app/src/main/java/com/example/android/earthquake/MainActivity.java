package com.example.android.earthquake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList< EarthquakeDescription> > {

    private static final String SAMPLE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final int M_id = 1;
    private EarthquakeAdapter adapter;
    TextView emptyView;
    View progressBar;
    ArrayList<EarthquakeDescription> earthquakeDescriptions=new ArrayList<EarthquakeDescription>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=(ListView) findViewById(R.id.list_view);
        progressBar=(View) findViewById(R.id.loading_spinner);
        adapter=new EarthquakeAdapter(this,earthquakeDescriptions);
        emptyView=findViewById(R.id.empty_view);
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if(!isConnected) {
            Log.i("No internet","Error Found");
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.network_issue);
        }
        else {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        }

        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);
    }
    @Override
    public Loader<ArrayList<EarthquakeDescription>> onCreateLoader(int i, Bundle bundle) {
        //create a uri so that it may helpful in taking user preferences
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        String min_magnitude=sharedPreferences.getString(getString(R.string.magnitude_key)
                ,getString(R.string.default_magnitude));
        String orderBy=sharedPreferences.getString(getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        Uri baseuri=Uri.parse(SAMPLE_URL);
        Uri.Builder uribuilder=baseuri.buildUpon();
        uribuilder.appendQueryParameter("format","geojson");
        uribuilder.appendQueryParameter("limit","15");
        uribuilder.appendQueryParameter("minmag",min_magnitude);
        uribuilder.appendQueryParameter("orderby",orderBy);
        return new EarthquakeLoader(this,uribuilder.toString());
    }
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<EarthquakeDescription>> loader,
                               ArrayList<EarthquakeDescription> earthquakeDescriptions) {


        adapter.clear();
        if (earthquakeDescriptions == null && earthquakeDescriptions.isEmpty()) {
        return;
        }
           adapter.add(earthquakeDescriptions);
            updateUI(earthquakeDescriptions);
        progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_found);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<EarthquakeDescription>> loader) {
        adapter.clear();
    }

    private void updateUI(ArrayList<EarthquakeDescription> earthquakes){
        final EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter( this ,earthquakes ) ;
        ListView listView = (ListView) findViewById(R.id.list_view) ;
        listView.setAdapter(earthquakeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthquakeDescription currentEarthquake =(EarthquakeDescription) earthquakeAdapter.getItem(position) ;
                Uri earthquakeUri = Uri.parse(currentEarthquake.getMurl()) ;
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri) ;
                startActivity( websiteIntent );
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}




