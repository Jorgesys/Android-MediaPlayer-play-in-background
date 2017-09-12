package com.jorgesys.musicbackground;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jorgesys on 02/02/2015.
 */

/* Add declaration of this service into the AndroidManifest.xml inside application tag*/

public class BackgroundSoundService extends Service {

    private static final String TAG = "BackgroundSoundService";
    MediaPlayer player;

    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()" );
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        player = MediaPlayer.create(this, R.raw.jorgesys_song);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        Toast.makeText(this, "Service started...", Toast.LENGTH_SHORT).show();
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");
        if(Preferences.getMediaPosition(getApplicationContext())>0){
            Log.i(TAG, "onStartCommand(), position stored, continue from position : " + Preferences.getMediaPosition(getApplicationContext()));
            player.start();
            player.seekTo(Preferences.getMediaPosition(getApplicationContext()));
        }else {
            Log.i(TAG, "onStartCommand() Start!...");
            player.start();
        }
        //re-create the service if it is killed.
        return Service.START_STICKY;
    }

    public IBinder onUnBind(Intent arg0) {
        Log.i(TAG, "onUnBind()");
        return null;
    }

    public void onStop() {
        Log.i(TAG, "onStop()");
        Preferences.setMediaPosition(getApplicationContext(), player.getCurrentPosition());
    }

    public void onPause() {
        Log.i(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() , service stopped! Media position: " + player.getCurrentPosition());
        //Save current position before destruction.
        Preferences.setMediaPosition(getApplicationContext(), player.getCurrentPosition());
        player.pause();
        player.release();
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
        Preferences.setMediaPosition(getApplicationContext(), player.getCurrentPosition());
    }

    //Inside AndroidManifest.xml add android:stopWithTask="false" to the Service definition.
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "onTaskRemoved(), save current position: " + player.getCurrentPosition());
        //instead of stop service, save the current position.
        //stopSelf();
        Preferences.setMediaPosition(getApplicationContext(), player.getCurrentPosition());
    }

}
