
package com.lelazy.yuanting.aboutYouxi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.lelazy.yuanting.MainActivity;
import com.lelazy.yuanting.R;
import com.lelazy.yuanting.classes.nettools;
import com.lelazy.yuanting.classes.pingzi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Date;

public class pingzi_C_produce extends AppCompatActivity {
    EditText text;
    String songname,song_position;
    int Gamescore;
    pingzi created_pingzi;
    String out_dir;
    Handler make_C_hander=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0X625:
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingzi_cproduce);
        text=findViewById(R.id.youxi_pingzi_produce);
        songname=getIntent().getStringExtra("songname");
        song_position=getIntent().getStringExtra("song_position");
        Gamescore=getIntent().getIntExtra("gamescore",0);
        created_pingzi =new pingzi();
        created_pingzi.setCatagory(3);
        created_pingzi.setCreatetime(new Date());
        out_dir=getApplicationContext().getExternalFilesDir("")+"/lelazy"+"/"+"localcreated/"+MainActivity.currentuser.getUid().toString();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0X753,0,"投放");
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0X753:
                if (!text.getText().toString().equals("")&&!text.getText().toString().equals(" ")){
                    created_pingzi.setPingzitext("来自用户"+ MainActivity.currentuser.getUsername()+"(id:"+MainActivity.currentuser.getUid()
                            +")的游戏记录：——"+Gamescore+"分\n"+text.getText().toString()+"\n——求挑战\n"+created_pingzi.getDateformat());
                    created_pingzi.setMusicUri(song_position);
                    created_pingzi.setPingziid(MainActivity.currentuser.getUid()+"-"+created_pingzi.getDateformat());
                    created_pingzi.setUserid(MainActivity.currentuser.getUid());
                    try {
                        save_local();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(pingzi_C_produce.this,"您多少写点儿",Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    void save_local() throws IOException {
        String after_name=song_position.substring(song_position.lastIndexOf("."));
        String externaldir= getApplicationContext().getExternalFilesDir("")+"/lelazy";
        ObjectOutputStream of;
        InputStream readfiles;
        OutputStream pingzisolid,writefiles;
        BufferedOutputStream bfo;
        BufferedInputStream bfi;
        File outer=new File(externaldir+"/"+"localcreated/"+MainActivity.currentuser.getUid()+"/"+created_pingzi.getPingziid());
        if (!outer.exists()){
            outer.mkdirs();
        }
        File pzdir=new File(outer.getAbsolutePath());
        pzdir.mkdirs();
        File wenjian=new File(pzdir.getAbsoluteFile()+"/"+created_pingzi.getPingziid()+".pingzi");
        pingzisolid=new FileOutputStream(wenjian);
        of=new ObjectOutputStream(pingzisolid);
        of.writeObject(created_pingzi);
        readfiles=new FileInputStream(new File(song_position));
        writefiles=new FileOutputStream(pzdir.getAbsoluteFile()+"/"+created_pingzi.getPingziid()+after_name);
        bfi=new BufferedInputStream(readfiles);
        bfo=new BufferedOutputStream(writefiles);
        int tmp;
        while ((tmp=bfi.read())!=-1){
            bfo.write(tmp);
            bfo.flush();
        }
        bfi.close();
        bfo.close();
        readfiles.close();
        writefiles.close();
        of.close();
        pingzisolid.close();
        uploadpingzi(externaldir+"/"+"localcreated/"+MainActivity.currentuser.getUid(),created_pingzi.getPingziid());
        make_C_hander.sendEmptyMessage(0X625);
    }
    void uploadpingzi(String externalpath,String pingziid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(externalpath + "/" + pingziid);
                for (int i = 0; i < f.listFiles().length; i++) {
                    try {
                        File ff = f.listFiles()[i];
                        nettools.netmethods.uploadfiles(ff);
                        System.out.println("看好了，大小是" + ff.length());
                        finish();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}