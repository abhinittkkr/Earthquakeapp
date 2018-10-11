package com.example.android.earthquake;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class QueryUtils {

    private static String url;

    private QueryUtils() {
    }
    public static ArrayList<EarthquakeDescription> fetchEarthquakeData(String  Url){
        URL murl=getUrl(Url);
        String jsonObjectString = "";
            try{
                jsonObjectString = makehttpRequest(murl) ;
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<EarthquakeDescription> earthquakes = extractEarthquake ( jsonObjectString );


            return earthquakes ;
    }
    private static URL getUrl(String url) {

        URL Url =  null ;

        try{
            Url = new URL(url);
        }
        catch (  MalformedURLException e  ){
            Log.e("in getUrl method"," bad url formed ") ;
        }
        return Url ;
    }

    private static String makehttpRequest ( URL url ) throws IOException {
        String jsonObjectString = "" ;
        InputStream inputStream = null ;
        HttpURLConnection httpURLConnection = null ;

        try{
            httpURLConnection = (HttpURLConnection) url.openConnection() ;
            httpURLConnection.setRequestMethod( "GET" );
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            jsonObjectString = inputstreamToString( inputStream ) ;
        }
        catch (IOException e){
            throw e;
        }finally {
            if( httpURLConnection!=null )
                httpURLConnection.disconnect();
            if( inputStream != null )
                inputStream.close();
        }

        return jsonObjectString ;
    }

    private static String inputstreamToString(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder() ;
        if ( inputStream != null ){
            InputStreamReader inputStreamReader = new InputStreamReader( inputStream , Charset.forName("UTF-8")) ;
            BufferedReader bufferedReader = new BufferedReader( inputStreamReader ) ;
            String line = bufferedReader.readLine() ;
            while( line != null ) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<EarthquakeDescription> extractEarthquake ( String jsonObjectString ){
        ArrayList<EarthquakeDescription> earthquakes = new ArrayList<EarthquakeDescription>() ;

        try {
            JSONObject rootObject = new JSONObject(jsonObjectString) ;
            JSONArray featureaArray = rootObject.getJSONArray("features") ;

            for(int i=0;i<featureaArray.length();i++){
                JSONObject cuurrentFeature = featureaArray.getJSONObject(i) ;
                JSONObject property = cuurrentFeature.getJSONObject( "properties" );
                double magnitude = property.getDouble( "mag" ) ;
                String name = property.getString("place") ;
                long time = property.getLong("time") ;
                String url = property.getString("url");

                Date dateObject = new Date(time) ;
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM , yyyy / hh:mm a") ;
                String dateToDisplay = dateFormatter.format(dateObject) ;

                earthquakes.add( new EarthquakeDescription(magnitude,name,dateToDisplay,url) ) ;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return earthquakes ;
    }

}
