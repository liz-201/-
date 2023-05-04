package com.lelazy.yuanting.aboutXuexi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Date;

public class pingzi_B_produce extends AppCompatActivity {
    EditText tucao_text;
    String catagory,section;
    pingzi made_tucao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingzi_b_produce);
        tucao_text=findViewById(R.id.xuexi_tucao_text);
        catagory=getIntent().getStringExtra("course_name");
        section=getIntent().getStringExtra("course_section");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"吐槽").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==1){
            System.out.println(catagory+section);
//            执行保存为实体并且上传的操作
            if (!tucao_text.getText().toString().equals("")){
                made_tucao=new pingzi();
                made_tucao.setCreatetime(new Date());
                String before= MainActivity.currentuser.getUsername()+"(id为"+MainActivity.currentuser.getUid()+")吐槽"+catagory+"第"+section+"集：\n";
                String after= made_tucao.getDateformat();
                made_tucao.setUserid(MainActivity.currentuser.getUid());
                made_tucao.setPingziid(MainActivity.currentuser.getUid()+"-"+made_tucao.getDateformat());
                made_tucao.setPingzitext(before+tucao_text.getText()+"\n"+after);
                savepingzi_local();
            }
            else {
                Toast.makeText(this,"请多少说点儿",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    void savepingzi_local(){
        String externaldir= getApplicationContext().getExternalFilesDir("")+"/lelazy";
        String usrdir;
        usrdir=MainActivity.currentuser.getUid().toString();
        InputStream readfiles;
        OutputStream pingzisolid,writefiles;
        ObjectOutputStream of;
//        dir创建到用户目录为止
        File dir=new File(externaldir+"/"+"localcreated/"+usrdir);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File pzdir=new File(dir.getAbsolutePath()+"/"+made_tucao.getPingziid());
        pzdir.mkdirs();
        try {
            File wenjian=new File(pzdir.getAbsoluteFile()+"/"+made_tucao.getPingziid()+".pingzi");
            pingzisolid=new FileOutputStream(wenjian);
            of=new ObjectOutputStream(pingzisolid);
            made_tucao.setCatagory(2);
            of.writeObject(made_tucao);
            of.close();
            pingzisolid.close();
            uploadpingzi(externaldir+"/"+"localcreated/"+usrdir,made_tucao.getPingziid());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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