package com.lelazy.yuanting.aboutPingzi;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.lelazy.yuanting.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class Playmusicurl_service extends IntentService {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.lelazy.yuanting.aboutPingzi.action.FOO";
    private static final String ACTION_BAZ = "com.lelazy.yuanting.aboutPingzi.action.BAZ";
    public static boolean isPlaying=true;
    public static boolean isPause=false;
    MediaPlayer mediaPlayerUrl=new MediaPlayer();

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.lelazy.yuanting.aboutPingzi.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.lelazy.yuanting.aboutPingzi.extra.PARAM2";

    public Playmusicurl_service() {
        super("Playmusic_service");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Playmusicurl_service.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Playmusicurl_service.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onHandleIntent(Intent intent) {
        /*if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }*/
//        String playeduri=intent.getStringExtra("uri");
//        0是本地播放，1是网络播放
        Integer mode=intent.getIntExtra("mode",-1);
        Integer useruri=intent.getIntExtra("uri1",-1);
        String pingziid=intent.getStringExtra("uri2");
        String resuri=intent.getStringExtra("uri3");
        isPlaying=true;
        String playeduri="";
        String host_ip=getString(R.string.host_ip);
        String host_port=getString(R.string.host_port);
        switch (mode){
            case 0:
                playeduri=getApplicationContext().getExternalFilesDir("")+"/lelazy/saved/"+useruri+"/"+pingziid+"/"+pingziid+resuri;
                break;
            case 1:
                playeduri="http://"+host_ip+":"+host_port+"/pingzi/"+useruri+"/"+pingziid+"/"+pingziid+resuri;
                break;
        }
        try {
                mediaPlayerUrl.setDataSource(playeduri);
                mediaPlayerUrl.prepareAsync();
                mediaPlayerUrl.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayerUrl.start();
                    }
                });
                while (isPlaying){
                    if (isPause){
                        mediaPlayerUrl.pause();
                    }
                    if (!mediaPlayerUrl.isPlaying()&&!isPause){
                        mediaPlayerUrl.start();
                    }
                    if (!isPlaying){
                        mediaPlayerUrl.stop();
                        isPause=false;
                        stopSelf();
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}