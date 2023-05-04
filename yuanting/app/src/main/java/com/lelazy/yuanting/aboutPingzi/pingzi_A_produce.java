package com.lelazy.yuanting.aboutPingzi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class pingzi_A_produce extends AppCompatActivity {

    EditText content;
    final int INSERTPIC=0X987;
    final int INSERTMUS=0X876;
    ImageSpan img;
    SpannableString spantext;
    Bitmap pic;
    public static final int PICK_PHOTO = 102;
    public static final int PICK_MUSIC=103;
    pingzi editedpingzi;
    Uri imguri,mp3uri,tempimguri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingzi_produce);
        ActionBar bar=getSupportActionBar();
        content=findViewById(R.id.pingzi_edit_content);
        registerForContextMenu(content);
        editedpingzi=new pingzi();
        imguri=null;
        mp3uri=null;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,INSERTPIC,0,"插入图片");
        menu.add(0,INSERTMUS,0,"插入音乐");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case INSERTPIC:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                }else {
                    //打开相册
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //Intent.ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent,PICK_PHOTO); // 打开相册
                }
                break;
            case INSERTMUS:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                }else {
                    //打开相册
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //Intent.ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
                    intent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("audio/*");
                    startActivityForResult(intent,PICK_MUSIC); // 打开相册
                }
                break;

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0X294,0,"抛向大海").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0X294:
                if (!content.getText().toString().equals("")& editedpingzi.getMusicUri()!=null){
//                    执行扔出瓶子的方法，封装瓶子的文字图片和音乐，通过网络传给服务器,复现瓶子的时候要把图片显示到picposition的地方
                    editedpingzi.setPingzitext(content.getText().toString());
                    editedpingzi.setPicposition(content.getSelectionStart());
                    editedpingzi.setCreatetime(new Date());
                    editedpingzi.setPingziid(MainActivity.currentuser.getUid()+"-"+editedpingzi.getDateformat());
                    editedpingzi.setUserid(MainActivity.currentuser.getUid());
                    savepingzi_local();
                    Toast.makeText(this,"扔出去咯",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    new AlertDialog.Builder(this).setMessage("请确保音乐文字输入哦~")
                            .setPositiveButton("明白", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri u;
        int cusor;
        switch (requestCode){
            case PICK_PHOTO:

                u=data.getData();
                try {
                    InputStream in=getContentResolver().openInputStream(u);
                    System.out.println(in.available());
                    if (in.available()>10*1024*1024){
                        Toast.makeText(this,"文件请小于10MB",Toast.LENGTH_SHORT).show();
                        break;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imguri=data.getData();
                editedpingzi.setImgUri(u.getPath());
                try {
                    pic=BitmapFactory.decodeStream(getContentResolver().openInputStream(u));
                    img=new ImageSpan(pic);
                    content.append(" ");
                    spantext=new SpannableString(content.getText());
                    //解决在文段开头就插入图片的情况，就是创造空格内容
                    if (content.getText().length()==0){
                        content.setText(" ");
                    }
                    spantext.setSpan(img,content.getSelectionStart()-1,content.getSelectionStart(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    cusor=content.getSelectionStart();
                    content.setText(spantext);
                    content.setSelection(cusor);
                    editedpingzi.setPicposition(content.getSelectionStart());
                    /*editedpingzi.setCreatetime(new Date());
                    if (MainActivity.currentuser!=null){
                        editedpingzi.setUser(MainActivity.currentuser);
                        savepingzi_local();
                    }*/
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case PICK_MUSIC:
                u=data.getData();
                try {
                    InputStream in=getContentResolver().openInputStream(u);
                    System.out.println(in.available());
                    if (in.available()>10*1024*1024){
                        Toast.makeText(this,"文件请小于10MB",Toast.LENGTH_SHORT).show();
                        break;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp3uri=data.getData();
                editedpingzi.setMusicUri(u.getPath());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    void savepingzi_local(){
        String externaldir= getApplicationContext().getExternalFilesDir("")+"/lelazy";
        String usrdir,musicgeshi="",imggeshi="";
        usrdir=MainActivity.currentuser.getUid().toString();
        if (mp3uri!=null){
            musicgeshi=mp3uri.getPath().substring(mp3uri.getPath().lastIndexOf("."));
//            Log.d("格格您迹象", musicgeshi);
        }
        if (imguri!=null){
            imggeshi=imguri.getPath().substring(imguri.getPath().lastIndexOf("."));
//            Log.d("格格您迹象", imggeshi);
        }
        InputStream readfiles;
        OutputStream pingzisolid,writefiles;
        BufferedOutputStream bfo;
        BufferedInputStream bfi;
        ObjectOutputStream of;
//        dir创建到用户目录为止
        File dir=new File(externaldir+"/"+"localcreated/"+usrdir);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File pzdir=new File(dir.getAbsolutePath()+"/"+editedpingzi.getPingziid());
        pzdir.mkdirs();
        try {
            File wenjian=new File(pzdir.getAbsoluteFile()+"/"+editedpingzi.getPingziid()+".pingzi");
            pingzisolid=new FileOutputStream(wenjian);
            of=new ObjectOutputStream(pingzisolid);
            editedpingzi.setCatagory(1);
            of.writeObject(editedpingzi);
//            将附带文件复制到瓶子目录下
            if (musicgeshi!=""){
                readfiles=getContentResolver().openInputStream(mp3uri);
                writefiles=new FileOutputStream(pzdir.getAbsoluteFile()+"/"+editedpingzi.getPingziid()+musicgeshi);
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
            }
            if (!imggeshi.equals("")){
                readfiles= (FileInputStream) getContentResolver().openInputStream(imguri);
                writefiles=new FileOutputStream(pzdir.getAbsoluteFile()+"/"+editedpingzi.getPingziid()+imggeshi);
                bfi=new BufferedInputStream(readfiles);
                bfo=new BufferedOutputStream(writefiles);
                int tmp;
                while ((tmp=bfi.read())!=-1/*bfi.read()!=-1*/){
                    writefiles.write(tmp);
                    writefiles.flush();
                }
                bfi.close();
                bfo.close();
                readfiles.close();
                writefiles.close();
            }
            of.close();
            pingzisolid.close();
            uploadpingzi(externaldir+"/"+"localcreated/"+usrdir,editedpingzi.getPingziid());
//            接下来执行上传服务器的功能，在服务器端的存储也同样为用户/瓶子id/，瓶子的文件,包括图片和音乐附在此目录下的子文件下，数据库记录瓶子id和作者，以及状态
//            上传瓶子的时候就对文件进行二次命名，然后放在服务器端同一个文件夹下，服务器在数据库记录下瓶子的资源位置，没有就是null
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp3uri=null;
        imguri=null;
    }
    void uploadpingzi(String externalpath,String pingziid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(externalpath + "/" + pingziid);
                for (int i = 0; i < f.listFiles().length; i++) {
                    try {
                        File ff=f.listFiles()[i];
                        nettools.netmethods.uploadfiles(ff);
                        System.out.println("看好了，大小是"+ff.length());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        /*File f = new File(externalpath + "/" + pingziid);
        for (int i = 0; i < f.listFiles().length; i++) {
            File fff=f.listFiles()[i];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        nettools.netmethods.uploadsingle(MainActivity.currentuser.getUid().toString(),fff.getName(),fff);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("整完乐意");
                }
            }).start();
        }*/
//        要判断上传的文件包含哪些，是否有图片
//        实现上传至服务器的操作，并且删除本地文件以及目录
    }
/*
    private void handleimg(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        // 根据图片路径显示图片
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            pic=bitmap;
        } else {
            Toast.makeText(this, "获取相册图片失败", Toast.LENGTH_SHORT).show();
        }
    }*/
}