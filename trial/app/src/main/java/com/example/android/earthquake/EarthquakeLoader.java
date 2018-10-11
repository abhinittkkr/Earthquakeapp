package com.example.android.earthquake;

import android.content.Context;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;
import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<EarthquakeDescription>> {
    private String mUrl;
    public EarthquakeLoader(Context context,String url){
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<EarthquakeDescription> loadInBackground() {
        if(mUrl==null)
        return null;
        return QueryUtils.fetchEarthquakeData(mUrl);
    }
}
