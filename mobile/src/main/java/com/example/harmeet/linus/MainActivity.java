package com.example.harmeet.linus;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.* ;
import android.widget.Toolbar;
import android.widget.ImageButton;

import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Random;


import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;
import android.net.Uri ;

public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback, SeekBar.OnSeekBarChangeListener, OnCompletionListener  {


    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "4fd18e4df0644c2caa5b97002bd1c0e5";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "http://linus.com/callback/";

    // Request code that will be passed together with authentication result to the onAuthenticationResult callback
    // Can be any integer
    private static final int REQUEST_CODE = 1337 ;

    private CardContainer mCardContainer;
    private Player mPlayer;
    private MediaPlayer mp ;
    private Handler mHandler = new Handler();
    private Utilities utils;
    private SongsManager songManager;
    final ArrayList<String> songList = new ArrayList<String>();
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();



    // define buttons using class ImageButton

    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            //All buttons
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songTitleLabel = (TextView) findViewById(R.id.songTitle);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        utils = new Utilities();
        songManager = new SongsManager();
        songsList = songManager.getPlayList();



        //Authentication
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"}) ;
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


        //Cards
        mCardContainer = (CardContainer) findViewById(R.id.layoutview);
        mCardContainer.setOrientation(Orientations.Orientation.Ordered);
        SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(this);
        adapter.add(new CardModel("Big Boobs", "Punjabi fuddi", getResources().getDrawable(R.drawable.picture1))); // adding cards here
        mCardContainer.setAdapter(adapter);
        CardModel card = new CardModel("Title1", "Description goes here", getResources().getDrawable(R.drawable.picture1)); // description goes here for the song

        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        mp.setOnCompletionListener(this); // Important


        /**
         * Play button click event
         * plays a song and changes button to pause image
         * pauses a song and changes button to play image
         * */
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (mp.isPlaying()) {
                    if (mp != null) {
                        mp.pause();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                } else {
                    // Resume song
                    if (mp != null) {
                        mp.start();
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }

            }
        });

        /**
         * Forward button click event
         * Forwards song specified seconds
         * */
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= mp.getDuration()) {
                    // forward song
                    mp.seekTo(currentPosition + seekForwardTime);
                } else {
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if (currentPosition - seekBackwardTime >= 0) {
                    // forward song
                    mp.seekTo(currentPosition - seekBackwardTime);
                } else {
                    // backward to starting position
                    mp.seekTo(0);
                }

            }
        });

        /**
         * Next button click event
         * Plays next song by taking currentSongIndex + 1
         * */
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if (currentSongIndex < (songsList.size() - 1)) {
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    // play first song
                    playSong(0);
                    currentSongIndex = 0;
                }

            }
        });

        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
//        btnPrevious.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                if(currentSongIndex > 0){
//                    playSong(currentSongIndex - 1);
//                    currentSongIndex = currentSongIndex - 1;
//                }else{
//                    // play last song
//                    playSong(songsList.size() - 1);
//                    currentSongIndex = songsList.size() - 1;
//                }
//
//            }
//        });

        /**
         * Button Click event for Repeat button
         * Enables repeat flag to true
         * */
        btnRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isRepeat){
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }else{
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }
            }
        });

        /**
         * Button Click event for Shuffle button
         * Enables shuffle flag to true
         * */
        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isShuffle){
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }else{
                    // make repeat to true
                    isShuffle= true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }
            }
        });



       //Initializing songs list

//        final ArrayList<String> songList = new ArrayList(); // want to use MediaPlayer as an identifier

//        songList.add("spotify:playlist:6kNuZA5SeXupyMiO0Ka9Az");
//        songList.add("spotify:track:4NpDZPwSXmL0cCTaJuVrCw");

        card.setOnClickListener(new CardModel.OnClickListener() {
            @Override
            public void OnClickListener() {
                Log.i("Swipeable Cards", "I am pressing the card");
            }
        });

        card.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
            @Override
            public void onLike() {
                Log.d("Swipeable Card", "I liked it");
            }

            @Override
            public void onDislike() {
                Log.d("Swipeable Card", "I did not liked it");
            }
        });

    // Button for pause with functionality here
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.pause();
            }
        });
        // Button for play with functionality here
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPlayer.play("spotify:track:SOAQCDC12A8C139378");
                mPlayer.play("spotify:user:22ynnkvxbl7xksfsygy442vji:playlist:6kNuZA5SeXupyMiO0Ka9Az");
            }
        });

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.skipToNext();
            }
        });

        FloatingActionButton fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.play("spotify:user:22ynnkvxbl7xksfsygy442vji:playlist:6kNuZA5SeXupyMiO0Ka9Az");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                        //mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }



    // All functions for play, stop, pause, update seekbar

//    * Function to play a song
//    * @param songIndex - index of song
//    * */




    public void  playSong(int songIndex){
        // Play song
        try {
            mp.reset();
           // mp.setDataSource(songsList.get(songIndex).get("songPath"));
            Uri test = Uri.parse("spotify:track:SOAQCDC12A8C139378");
           // String uri = "spotify:track:SOAQCDC12A8C139378";

            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(this,test);
            mp.prepare();
            mp.start();
           // mPlayer.play("spotify:track:SOAQCDC12A8C139378");
            // Displaying Song title
            String songTitle = songsList.get(songIndex).get("songTitle");
            songTitleLabel.setText(songTitle);

            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.btn_pause);

            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

            /**
             * Update timer on seekbar
             * */
            public void updateProgressBar() {
                mHandler.postDelayed(mUpdateTimeTask, 100);
            }

            /**
             * Background Runnable thread
             * */
            private Runnable mUpdateTimeTask = new Runnable() {
                public void run() {
                    long totalDuration = mp.getDuration();
                    long currentDuration = mp.getCurrentPosition();

                    // Displaying Total Duration time
                    songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
                    // Displaying time completed playing
                    songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

                    // Updating progress bar
                    int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                    //Log.d("Progress", ""+progress);
                    songProgressBar.setProgress(progress);

                    // Running this thread after 100 milliseconds
                    mHandler.postDelayed(this, 100);
                }
            };

            /**
             *
             * */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

            }

            /**
             * When user starts moving the progress handler
             * */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // remove message Handler from updating progress bar
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            /**
             * When user stops moving the progress hanlder
             * */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mp.getDuration();
                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                mp.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();
            }

            /**
             * On Song Playing completed
             * if repeat is ON play same song again
             * if shuffle is ON play random song
             * */
            @Override
            public void onCompletion(MediaPlayer arg0) {

                // check for repeat is ON or OFF
                if (isRepeat) {
                    // repeat is on play same song again
                    playSong(currentSongIndex);
                } else if (isShuffle) {
                    // shuffle is on - play a random song
                    Random rand = new Random();
                    currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
                    playSong(currentSongIndex);
                } else {
                    // no repeat or shuffle ON - play next song
                    if (currentSongIndex < (songsList.size() - 1)) {
                        playSong(currentSongIndex + 1);
                        currentSongIndex = currentSongIndex + 1;
                    } else {
                        // play first song
                        playSong(0);
                        currentSongIndex = 0;
                    }
                }
            }


            // Functions for login controls and everything
            @Override
            public void onLoggedIn() {
                Log.d("MainActivity", "User logged in");
            }

            @Override
            public void onLoggedOut() {
                Log.d("MainActivity", "User logged out");
            }

            @Override
            public void onLoginFailed(Throwable error) {
                Log.d("MainActivity", "Login failed");
                Log.d("MainActivity", error.getMessage());
            }

            @Override
            public void onTemporaryError() {
                Log.d("MainActivity", "Temporary error occurred");
            }

            @Override
            public void onConnectionMessage(String message) {
                Log.d("MainActivity", "Received connection message: " + message);
            }

            @Override
            public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
                Log.d("MainActivity", "Playback event received: " + eventType.name());
                switch (eventType) {
                    // Handle event type as necessary
                    default:
                        break;
                }
            }

            @Override
            public void onPlaybackError(ErrorType errorType, String errorDetails) {
                Log.d("MainActivity", "Playback error received: " + errorType.name());
                switch (errorType) {
                    // Handle error type as necessary
                    default:
                        break;
                }
            }

            @Override
            protected void onDestroy() {
                // VERY IMPORTANT! This must always be called or else you will leak resources
                Spotify.destroyPlayer(this);
                super.onDestroy();
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }

        }

