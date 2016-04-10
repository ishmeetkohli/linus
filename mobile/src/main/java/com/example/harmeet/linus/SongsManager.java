package com.example.harmeet.linus;

/**
 * Created by Harmeet on 2016-04-09.
 */

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

public class SongsManager {

   // final String MEDIA_PATH = new String("/sdcard/");
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    // Constructor
    public SongsManager(){

    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
    public ArrayList<HashMap<String, String>> getPlayList(){
       // File home = new File(MEDIA_PATH);

       // if (home.listFiles(new FileExtensionFilter()).length > 0) {
          //  for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle","test" );
                song.put("songPath", "spotify:track:SOAQCDC12A8C139378" );

                // Adding each song to SongList
        songsList.add(song);


        // return songs list array
        return songsList;
    }

    /**
     * Class to filter files which are having .mp3 extension
     * */
//    class FileExtensionFilter implements FilenameFilter {
//        public boolean accept(File dir, String name) {
//            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
//        }
//    }


}
