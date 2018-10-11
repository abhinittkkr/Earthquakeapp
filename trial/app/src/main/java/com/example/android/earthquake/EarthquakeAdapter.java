package com.example.android.earthquake;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter {


    public EarthquakeAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if( convertView == null ){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_listitem ,parent,false) ;
        }

        EarthquakeDescription currentWord = (EarthquakeDescription) getItem(position) ;

        TextView magnitudeView = (TextView)  convertView.findViewById(R.id.magnitude_textview);
        TextView locationOffsetView = (TextView) convertView.findViewById(R.id.location_offset_textview);
        TextView primaryLocationView = (TextView)  convertView.findViewById(R.id.primary_location_textview);
        TextView timeView =(TextView) convertView.findViewById(R.id.time_textview) ;
        TextView dateView = (TextView)  convertView.findViewById(R.id.date_textview);



        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground() ;
        int magnitudecolor = getMagnitudeColor( currentWord.getM() ) ;
        magnitudeCircle.setColor(magnitudecolor);

        DecimalFormat formatter = new DecimalFormat("0.0") ;
        String magnitude = formatter.format(currentWord.getM()) ;

        String location = currentWord.getMlocation();
        String seperatorString =  " of " ;
        String locationOffset;
        String primaryLocation ;
        if( location.contains(seperatorString) ){
            String[] locationParts = location.split(seperatorString);
            locationOffset = locationParts[0]+ seperatorString ;
            primaryLocation = locationParts[1];
        }
        else{
            locationOffset = "Near the" ;
            primaryLocation = location ;
        }

        String dateTime = currentWord.getMtime() ;
        seperatorString = "/" ;
        String[] dateParts = dateTime.split(seperatorString);
        String date = dateParts[0];
        String time = dateParts[1];


        magnitudeView.setText( magnitude);
        locationOffsetView.setText(locationOffset);
        primaryLocationView.setText( primaryLocation );
        timeView.setText(time);
        dateView.setText( date );

        return convertView ;

    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
