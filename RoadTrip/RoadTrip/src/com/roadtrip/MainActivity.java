package com.roadtrip;

import java.util.Collection;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Chart;
import de.umass.lastfm.User;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastFM();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void lastFM() {
    	 String key = "bda6ba1f6b4d727efd8b98377a5488c6"; //this is the key used in the Last.fm API examples
    	    String user = "JRoar";
    	    Chart<Artist> chart = User.getWeeklyArtistChart(user, 10, key);
    	    DateFormat format = DateFormat.getDateInstance();
    	    String from = format.format(chart.getFrom());
    	    String to = format.format(chart.getTo());
    	    System.out.printf("Charts for %s for the week from %s to %s:%n", user, from, to);
    	    Collection<Artist> artists = chart.getEntries();
    	    for (Artist artist : artists) {
    	        System.out.println(artist.getName());
    	    }
    }
    
}
