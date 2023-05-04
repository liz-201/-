package com.lelazy.yuanting.aboutYouxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lelazy.yuanting.MainActivity;
import com.lelazy.yuanting.R;
import com.lelazy.yuanting.aboutPingzi.Playmusicurl_service;
import com.lelazy.yuanting.classes.nettools;
import com.lelazy.yuanting.classes.pingzi;
import com.lelazy.yuanting.classes.pingzireadstream;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class youxiplay extends AppCompatActivity {
    youxi_bg_service bgservice;
    ServiceConnection serviceConnection;
    Intent to_bg;
//    block宽270，高300
    RelativeLayout way1,way2,way3,way4;
    FrameLayout line;
    RelativeLayout pause_page;
    ImageButton youxiPauseButton;
    ImageView youxiPausePlay,youxiPauseExit;
    int width,height;
    WindowManager manager;
    DisplayMetrics metrics;
    Timer movetime;
    List<String> nethouzhu;
    int gameeffect=100;
    public int Gamescorce,maxscorce;
    Thread youxithread;
    youxiRun run;
    pingzi x;
    int mode;
    String songname,tailname;
    public boolean task_run;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0X101:
                    Integer waycode=(Integer) msg.obj;
                    putblockonway(waycode);
                    break;
                case 0X333:
                   AlertDialog aa= new AlertDialog.Builder(youxiplay.this).setMessage("游戏结束，您的分数是："+Gamescorce)
                            .setTitle("游戏结束")
                            .setPositiveButton("投出瓶子", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent to_make_c=new Intent(youxiplay.this,pingzi_C_produce.class);
                                    to_make_c.putExtra("songname",songname);
                                    to_make_c.putExtra("song_position",pingziexdir+songname+"/"+songname+tailname);
                                    to_make_c.putExtra("gamescore",Gamescorce);
                                    startActivity(to_make_c);
                                    finish();
                                }
                            })
                            .setNegativeButton("累了，毁灭吧", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .create();
                   aa.setCanceledOnTouchOutside(false);
                   aa.show();

                    break;
            }
            super.handleMessage(msg);
        }
    };
    block_move a;
    TextView scorceview;
    SharedPreferences gamepre;
    SharedPreferences.Editor gamepreedit;
     public ArrayList<youxi_block> blocklist;
    String pingziexdir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_youxi);
        getSupportActionBar().hide();
        manager = getWindowManager();
        metrics = new DisplayMetrics();
        pingziexdir= getApplicationContext().getExternalFilesDir("")+"/lelazy"+"/saved/"+MainActivity.currentuser.getUid()+"/";
        gamepre=getSharedPreferences("Gamepoint",MODE_PRIVATE);
        mode=getIntent().getIntExtra("mode",-1);
        gamepreedit=gamepre.edit();
        movetime=new Timer();
        manager.getDefaultDisplay().getMetrics(metrics);//获取屏幕高宽
        width= metrics.widthPixels;  //以像素为单位
        height = metrics.heightPixels;
        run=new youxiRun(this.handler);
        youxithread=new Thread(run);
        maxscorce=gamepre.getInt("maxgamepoint",0);
        Gamescorce=0;
        to_bg=new Intent(this,youxi_bg_service.class);
        youxiPauseButton=findViewById(R.id.youxi_pause_button);
        way1=findViewById(R.id.youxi_pass_1);
        way2=findViewById(R.id.youxi_pass_2);
        way3=findViewById(R.id.youxi_pass_3);
        way4=findViewById(R.id.youxi_pass_4);
        line=findViewById(R.id.panding);
        blocklist=new ArrayList<>();
        a=new block_move();
        task_run=true;
//        通过本地pingzi文件的属性获取歌曲的资料
        if (mode==0){
            x=new pingzi();
            try {
                pingzireadstream ois=new pingzireadstream(new FileInputStream(pingziexdir+getIntent().getStringExtra("song_name")+"/"
                        +getIntent().getStringExtra("song_name")+".pingzi"));
                x=(pingzi) ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            songname=getIntent().getStringExtra("song_name");
            tailname=x.getMusicUri().substring(x.getMusicUri().lastIndexOf("."));
        }
//        以下是通过网络获取歌曲信息
        if (mode==1) {
            int author_id = getIntent().getIntExtra("author_id", -1);
            String pingziid = getIntent().getStringExtra("song_name");
            String ip = getString(R.string.host_ip);
            String port = getString(R.string.host_port);
            String res_part1 = "http://" + ip + ":" + port + "/" + "pingzi/";
            pingzi x = new pingzi();
            x.setUserid(author_id);
            x.setPingziid(pingziid);
            songname = res_part1 + author_id + "/" + pingziid + "/" + pingziid;
            tailname = getIntent().getStringExtra("tail_name");
        }
        pause_page=(RelativeLayout) getLayoutInflater().inflate(R.layout.youxi_pause_page,null);
        youxiPauseExit=pause_page.findViewById(R.id.youxi_pause_exit);
        scorceview=pause_page.findViewById(R.id.youxi_pause_score);
        serviceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                bgservice=((youxi_bg_service.bg_binder)service).getservice();
                if (mode==1){
                    ((youxi_bg_service.bg_binder) service).setsong(songname+tailname);
                }
                if (mode==0){
                    ((youxi_bg_service.bg_binder) service).setsong(pingziexdir+songname+"/"+songname+tailname);
                }

                System.out.println("执行力setsong");
                try {
                    bgservice.playerprepared();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                System.out.println("断了的弦");
                bgservice=null;
            }
        };
        bindService(to_bg,serviceConnection,BIND_AUTO_CREATE);
        System.out.println(pingziexdir+songname+"/"+songname+tailname);
        youxithread.start();
        youxiPauseExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run.setExit(true);
                movetime.cancel();
                unbindService(serviceConnection);
                finish();
            }
        });
        youxiPausePlay=pause_page.findViewById(R.id.youxi_pause_play);
        youxiPausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youxiPauseButton.setClickable(true);
                ViewGroup par=(ViewGroup) pause_page.getParent();
                par.removeView(pause_page);
                bgservice.playcontinue();
                run.setPause(false);
                task_run=true;
            }
        });
        youxiPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                task_run=false;
                bgservice.pause();
                run.setPause(true);
                scorceview.setText("得分为"+Gamescorce);
                addContentView(pause_page, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
        line.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x=event.getRawX();
                float y=event.getRawY();
                youxi_block tempblock;
                int[] location = new int[2];
                for (int i = 0; i < blocklist.size(); i++) {
                    tempblock=blocklist.get(i);
                    float bx=tempblock.getX();
                    float by=tempblock.getTop();
                    tempblock.getLocationInWindow(location);
                    int tx = location[0];
                    int ty = location[1];
                    if (x>=tx&&x<=tx+270&&y>=ty&&y<=ty+300){
                        Gamescorce=Gamescorce+1;
                        tempblock.setVisibility(View.GONE);
                        try {
                            blocklist.get(i).blockdes();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        blocklist.remove(i);
                        break;
                    }
                }
                return true;
            }
        });

    }
    void putblockonway(Integer integer){
        switch (integer){
            case 1:
                youxi_block w1b=new youxi_block(this);
                way1.addView(w1b);
                blocklist.add(w1b);
                break;
            case 2:
                youxi_block w2b=new youxi_block(this);
                way2.addView(w2b);
                blocklist.add(w2b);
                break;
            case 3:
                youxi_block w3b=new youxi_block(this);
                way3.addView(w3b);
                blocklist.add(w3b);
                break;
            case 4:
                youxi_block w4b=new youxi_block(this);
                way4.addView(w4b);
                blocklist.add(w4b);
                break;
        }
        movetime.schedule(new block_move(),0,gameeffect);
    }
    class block_move extends TimerTask{
        @Override
        public void run() {
            if (task_run){
                for (int i = 0; i < blocklist.size(); i++) {
                    float y=blocklist.get(i).getY();
                    if (blocklist.get(i).getY()>height){
                        try {
                            if (Gamescorce>maxscorce){
                                maxscorce=Gamescorce;
                                gamepreedit.putInt("maxgamepoint",maxscorce);
                                gamepreedit.commit();
                            }
                            handler.sendEmptyMessage(0X333);
//                            finish();
                            task_run=false;
                            bgservice.pause();
                            run.setPause(true);
                            blocklist.get(i).blockdes();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        blocklist.remove(i);
                        continue;
                    }
                    blocklist.get(i).setY(y+10);

                }
            }
        }
    }

}