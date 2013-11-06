package com.roadtrip;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Playlist;
import de.umass.lastfm.Radio;
import de.umass.lastfm.Session;
import de.umass.lastfm.Track;




public class MusicActivity extends Activity {

	static String key = "bda6ba1f6b4d727efd8b98377a5488c6"; 
	static String user = "freddysalinas80";
	static String password = "CS371M";
	static String secret = "7f634c5e83b3d0754ffa4c4fe8325cac";
	
	
	private Playlist playlist;
	private MediaPlayer mediaPlayer;
	private int trackIndex, duration, playlistLength, lengthOfTrip;
	private OnCompletionListener playNextSong;
	private Session session;
	
	public Track currentTrack;
	
	public String destination;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
		
		
        // Get the message from the intent
        Intent intent = getIntent();
        destination = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        
		
        // Create the text view
        TextView t =new TextView(this); 
        t = (TextView)findViewById(R.id.title); 
        t.setText(destination);
    
		
		
		
        Caller.getInstance().setCache(null);
        mediaPlayer = new MediaPlayer();
        playNextSong = new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				nextSong(null);
			}
        };
        trackIndex = 0;
        duration = 0;
        playlistLength = 0;
        nextSong(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music, menu);
		return true;
	}
	
	public void pauseSong(View view) {
		
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			System.out.printf("PAUSED SONG \n");
		} else {
			mediaPlayer.start();
			System.out.printf("RESUMED SONG \n");
		}
	}
	
	public void nextSong(View view) {
		if (trackIndex == playlistLength) {
			new getPlaylist().execute();
			System.out.printf("\n\t TRACK INDEX: %d \n", trackIndex);
			trackIndex=0;
			playlistLength = 0;
			new changeSong().execute(trackIndex);
			System.out.printf("NEXT SONG \n");
		} else {
			System.out.printf("\n\t TRACK INDEX: %d \n", trackIndex);
			new changeSong().execute(trackIndex);
			System.out.printf("NEXT SONG \n");
		}
	}

	public void previousSong(View view) {
//		trackIndex--;
//		new changeSong().execute(trackIndex);
//		System.out.printf("PREVIOUS SONG \n");
	}
	
	
    private class getPlaylist extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {	
			session = Authenticator.getMobileSession(user, password, key, secret); // authenticate user
//	   	 	Radio radio = Radio.tune(Radio.RadioStation.similarArtists("Vampire Weekend"), session); // tune into station
	   	 	Radio radio = Radio.tune(Radio.RadioStation.tagged(destination), session);

	   	 	//TODO: Add map update here

	        System.out.println("name: "+ radio.getStationName()); 
	        playlist = null;
	        playlist = radio.getPlaylist(); // retrieve the playlist

	        for (Track track : playlist.getTracks()) {
	        	System.out.printf("%s - %s: %s \n \t Duration: %d \n\n", track.getArtist(), track.getName(), track.getLocation(), track.getDuration());
	        	duration += track.getDuration();
	        	playlistLength++;
	        }
	        System.out.printf("FINISHED GETTING PLAYLIST\n");
	        return null;
		}
    }
    
    private class changeSong extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... index) {
			int indexOfPlaylist = index[0].intValue();
			System.out.printf("INDEXOFPLAYLIST: %d \n", indexOfPlaylist);
			Track t = (playlist.getTracks()).get(indexOfPlaylist);
			String location = t.getLocation();
		    mediaPlayer.reset();
		    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		    mediaPlayer.setOnCompletionListener(playNextSong);

		    try {
				mediaPlayer.setDataSource(location);
				mediaPlayer.prepare(); // might take long! (for buffering, etc)
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    mediaPlayer.start();
		    currentTrack = t;
	        trackIndex++;

		    System.out.printf("CURRENT SONG %s by %s\n", currentTrack.getName(), currentTrack.getArtist());
			return null;
		}
    }
}

