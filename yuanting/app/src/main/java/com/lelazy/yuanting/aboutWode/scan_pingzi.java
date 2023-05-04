package com.lelazy.yuanting.aboutWode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lelazy.yuanting.*;
import com.lelazy.yuanting.R;
import com.lelazy.yuanting.aboutPingzi.Playmusicurl_service;
import com.lelazy.yuanting.classes.Databasetool;
import com.lelazy.yuanting.classes.pingzi;
import com.lelazy.yuanting.classes.pingzireadstream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class scan_pingzi extends AppCompatActivity {
    Cursor pingzis_cursor;
    SQLiteDatabase pingzi_db;
    Databasetool dbtools;
    PopupWindow heihei;
    DisplayMetrics displayMetrics;
    int screenhei,screenwid;
    TextView pingzitext;
    BufferedInputStream bfreadpingzi;
    String pingziexdir;
    ListView scan_view;
    SimpleAdapter adapter;
    ArrayList<String> pingziidlist;
    String pingzipath;
    List<Map<String,Object>> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_pingzi);
        pingziexdir=getApplicationContext().getExternalFilesDir("")+"/lelazy"+"/saved/"+MainActivity.currentuser.getUid()+"/";
        ActionBar actionBar=getSupportActionBar();
        displayMetrics= getResources().getDisplayMetrics();
        screenwid=displayMetrics.widthPixels;
        screenhei=displayMetrics.heightPixels;
        dbtools=new Databasetool(this,getFilesDir().getPath()+"/pingzi_saved.db",null,1);
        View pop=getLayoutInflater().inflate(R.layout.pingzi_content_popwindow,null,false);
        heihei=new PopupWindow(pop,screenwid,screenhei);
        ImageButton closepop=pop.findViewById(R.id.pingzicontentclose);
        closepop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent play=new Intent(scan_pingzi.this, Playmusicurl_service.class);
                Playmusicurl_service.isPlaying=false;
                heihei.dismiss();
            }
        });
        pingzitext=pop.findViewById(R.id.pingzi_text);
        scan_view=findViewById(R.id.listview_scanpingzi);
        pingziidlist=new ArrayList<String>();
//        pingzi_db= SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath()+"/pingzi_saved.db",null);
        pingzi_db=dbtools.getWritableDatabase();
        pingzis_cursor=pingzi_db.query("saved_pingzis",null,null,null,null,null,null);
        if (pingzis_cursor.moveToFirst()){
            do {
                pingziidlist.add(pingzis_cursor.getString(0));
            }while (pingzis_cursor.moveToNext());
        }
        pingzis_cursor.close();
        int[] images=new int[pingziidlist.size()];
        for (int i = 0; i < pingziidlist.size(); i++) {
            images[i]=R.drawable.pingzi;
        }
        items=new ArrayList<Map<String,Object>>();
        for (int i=0;i<pingziidlist.size();i++){
            Map<String,Object> item=new HashMap<String,Object>();
            item.put("name",pingziidlist.get(i));
            item.put("pic", images[i]);
            items.add(item);
        }
        adapter=new SimpleAdapter(this,items,R.layout.scan_pingzi_listview_item,new String[]{
                "name","pic"
        },new int[]{
                R.id.scan_pingzi_text,R.id.scan_pingzi_image
        });
        scan_view.setAdapter(adapter);
        registerForContextMenu(scan_view);
        scan_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                heihei.showAsDropDown(view);
                pingzipath=pingziexdir+pingziidlist.get(position)+"/"+pingziidlist.get(position);
                pingzireadstream ois;
//                逻辑应该是根据表中id，查找对应目录，读取文件
                try {

//                    存在下载不完全的问题，显示错乱的问题


                    ois=new pingzireadstream(new FileInputStream(pingziexdir+pingziidlist.get(position)+"/"+pingziidlist.get(position)+".pingzi"));
                    pingzi x=(pingzi) ois.readObject();
//                    System.out.println(x.getMusicUri());
//                    记得还原
                    SpannableString content=new SpannableString(x.getPingzitext());
//                    Log.d("imgggggggg", x.getImgUri());
                    if (x.getImgUri()!=null){
//                        根据类的中记录的数据位置显示图片
                        Log.d("imgggggggg", x.getImgUri());
//                        java.io.FileNotFoundException: No content provider: /storage/emulated/0/Android/data/com.lelazy.yuanting/files/lelazy/saved/1/4/4.png
                        String geshi=x.getImgUri().substring(x.getImgUri().lastIndexOf("."));
                        if (Build.VERSION.SDK_INT>23){
                            int REQUEST=101;
                            String[] permission={
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            };
                            for (String str:
                                 permission) {
                                if (scan_pingzi.this.checkSelfPermission(str)!= PackageManager.PERMISSION_GRANTED){
                                    scan_pingzi.this.requestPermissions(permission,REQUEST);
                                    return;
                                }
                                else {
//                                    InputStream readfiles= (FileInputStream) getContentResolver().openInputStream(Uri.parse(pingzipath+geshi));
//                                    Bitmap pic= BitmapFactory.decodeStream((FileInputStream)getContentResolver().openInputStream(Uri.parse(pingzipath+geshi)));
                                    InputStream readfiles=new FileInputStream(pingzipath+geshi);
                                    Bitmap pic= BitmapFactory.decodeStream(readfiles);
                                    ImageSpan img=new ImageSpan(pic);
                                    content.setSpan(img,x.getPicposition()-1,x.getPicposition(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                        }
                    }
                    if (x.getMusicUri()!=null){
                        String geshi=x.getMusicUri().substring(x.getMusicUri().lastIndexOf("."));
                        Intent playmusic=new Intent(scan_pingzi.this,Playmusicurl_service.class);
                        playmusic.putExtra("mode",0);
                        playmusic.putExtra("uri1",x.getUserid());
                        playmusic.putExtra("uri2",x.getPingziid());
                        playmusic.putExtra("uri3",geshi);
                        startService(playmusic);
                    }
                    pingzitext.setText(content);
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.isCheckable()){
            item.setChecked(true);
        }
        switch (item.getItemId()){
            case R.id.scan_pingzi_return:
                finish();
                return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scanpingzi_top,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,0X655,0,"毁坏");
        menu.add(0,0X654,0,"放归");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        return super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case 0X655:
                String filename=pingziidlist.get(menuInfo.position);
                File f=new File(pingziexdir+filename);
                pingzi_db.execSQL("delete from saved_pingzis where pingzi_id="+filename);
                if (f.exists()){
                    for (File ff:f.listFiles()
                ) {
                    Log.d("文件有", ff.getAbsolutePath());
                    ff.delete();
                }
                }
                /*for (File ff:f.listFiles()
                ) {
                    Log.d("文件有", ff.getAbsolutePath());
                    ff.delete();
                }*/
                Log.d("length", String.valueOf(f.listFiles().length));
                f.delete();
                pingziidlist.remove(menuInfo.position);
                items.remove(menuInfo.position);
                adapter=new SimpleAdapter(this,items,R.layout.scan_pingzi_listview_item,new String[]{
                        "name","pic"
                },new int[]{
                        R.id.scan_pingzi_text,R.id.scan_pingzi_image
                });
                scan_view.setAdapter(adapter);
                break;
            case 0X654:

                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}