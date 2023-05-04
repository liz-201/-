package com.lelazy.yuanting.aboutYouxi;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

public class youxi_bg_service extends Service {
    String songname;
    MediaPlayer player;
    bg_binder bg_binder=new bg_binder();
    public youxi_bg_service() {
    }
    class bg_binder extends Binder{
        youxi_bg_service getservice(){
            return youxi_bg_service.this;
        }
        void setsong(String song){
            songname=song;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
       /* // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");*/
        System.out.println("牢牢绑住");
        return bg_binder;
    }
    void playerprepared() throws IOException {
        player=new MediaPlayer();
        player.setLooping(true);
        player.setDataSource(songname);
        System.out.println("player::::"+songname);
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });
        player.start();
    }
    void pause(){
        player.pause();
    }
    void playcontinue(){
        player.start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        return super.onUnbind(intent);
    }
}