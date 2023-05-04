package com.lelazy.yuanting.fragments;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lelazy.yuanting.MainActivity;
import com.lelazy.yuanting.aboutPingzi.Playmusicurl_service;
import com.lelazy.yuanting.aboutPingzi.pingzi_A_produce;
import com.lelazy.yuanting.aboutWode.wodeLoginDiago;
import com.lelazy.yuanting.aboutYouxi.youxiplay;
import com.lelazy.yuanting.classes.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lelazy.yuanting.R;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class fragpingzi extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final int pingzi_pick=0X666;
    final int pingzi_throw=0X667;
    final int pingzi_accept=0X668;
    Databasetool dbtool;
    SQLiteDatabase pingzi_db;
    pingziview temppingziview;
//    接下来几个变量用于记录当前的瓶子数据（从网络中获取到的）
    pingzi netpingzi=null;
    SpannableString netspannablestring=null;
    Bitmap netbitmap=null;
    ImageSpan netimgspan=null;
    List<String> nethouzhui=new ArrayList<>();
    pingzi p=null;
    Intent to_game;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public fragpingzi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dashfrag.
     */
    // TODO: Rename and change types and number of parameters
    public static fragpingzi newInstance(String param1, String param2) {
        fragpingzi fragment = new fragpingzi();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ViewFlipper mapflipper;
    ImageButton pingziMapPrev,pingziMapNext;
    PopupWindow heihei;
    View map1,map2,map3;
    View rootview;
//    此变量表示从服务器获取到的瓶子id列表
    public List<pingzi> pingzilist;
    DisplayMetrics displayMetrics;
    int chosenId;
    List<pingziview> pingziviewList;
    int screenhei,screenwid;
    TextView pingzitext;
    Handler createlisthandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
//            设置任务码，表明是初始化的时候进行的任务还是在老一次的任务
            switch (msg.what){
//                表示已经获取到pingzilist，可以生成pingziview
                case 111:
                    System.out.println(pingzilist.get(0).getUserid().toString());
                    createpingziview();
                    break;
//                    表示可以从服务器获取pingzi然后展示
                case 'Y':
                    netspannablestring=new SpannableString(netpingzi.getPingzitext());
                    if (netbitmap!=null){
                        netimgspan=new ImageSpan(netbitmap);
                        System.out.println(netbitmap.getByteCount());
                        netspannablestring.setSpan(netimgspan,netpingzi.getPicposition()-1,netpingzi.getPicposition(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent tomusic=new Intent(getContext(),Playmusicurl_service.class);
                            tomusic.putExtra("mode",1);
                            tomusic.putExtra("uri1",netpingzi.getUserid());
                            tomusic.putExtra("uri2",netpingzi.getPingziid());
                            tomusic.putExtra("uri3",nethouzhui.get(1));
                            getActivity().startService(tomusic);
                        }
                    }).start();
                    pingzitext.setText(netspannablestring);
                    heihei.showAsDropDown(rootview);
                    break;
                case 'A':
//                    代表再捞一次
                    System.out.println("收到");
                    getfragpingzi();
                    break;
                case 0X888:
                    to_game.putExtra("tail_name",nethouzhui.get(1));
                    startActivity(to_game);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        dbtool=new Databasetool(getContext(),getContext().getFilesDir().getPath()+"/pingzi_saved.db",null,1);
//        pingzi_db=SQLiteDatabase.openOrCreateDatabase(getContext().getFilesDir().getPath()+"/pingzi_saved.db",null);
        pingzi_db=dbtool.getWritableDatabase();
        displayMetrics= getResources().getDisplayMetrics();
        screenwid=displayMetrics.widthPixels;
        screenhei=displayMetrics.heightPixels;
        pingziviewList=new ArrayList<>();
        pingzilist=new ArrayList<pingzi>();
        nettools nt=new nettools(getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.page_pingzi, container, false);
        rootview=root;
        ActionBar actionBar=getActivity().getActionBar();
        View pop=inflater.inflate(R.layout.pingzi_content_popwindow,container, false);
        heihei=new PopupWindow(pop,screenwid,screenhei);
//        closepop按下的时候要关闭后台service音乐播放，heihei打开的时候进行音乐播放，播放的音乐就是根据瓶子id请求到的
        ImageButton closepop=pop.findViewById(R.id.pingzicontentclose);
        pingzitext=pop.findViewById(R.id.pingzi_text);
        closepop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent play=new Intent(getContext(),Playmusicurl_service.class);
                Playmusicurl_service.isPlaying=false;
                netpingzi=null;
                netbitmap=null;
                nethouzhui=null;
                netspannablestring=null;
                netimgspan=null;
                heihei.dismiss();
            }
        });
        mapflipper=root.findViewById(R.id.pingzi_maps);
        map1=(FrameLayout) inflater.inflate(R.layout.pingzi_maplayout,container,false);
        map2=(FrameLayout) inflater.inflate(R.layout.pingzi_maplayout,container,false);
        map3=(FrameLayout) inflater.inflate(R.layout.pingzi_maplayout,container,false);
        flipperFillMap();
        getfragpingzi();
//        createpingziview();
        pingziMapPrev=root.findViewById(R.id.pingzi_premap);
        pingziMapNext=root.findViewById(R.id.pingzi_nextmap);
        pingziMapNext.setOnClickListener(this);
        pingziMapPrev.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        netpingzi=null;
        netbitmap=null;
        nethouzhui=null;
        netspannablestring=null;
        netimgspan=null;
        switch (v.getId()){
            case R.id.pingzi_premap:
                heihei.dismiss();
                mapflipper.showPrevious();
                break;
            case R.id.pingzi_nextmap:
                heihei.dismiss();
                mapflipper.showNext();
                break;
        }
    }
//    其中添加地图，并实现点击后展开瓶子的视图
    void flipperFillMap(){
        ImageView m1,m2,m3;
        m1=map1.findViewById(R.id.bg_area);
        m2=map2.findViewById(R.id.bg_area);
        m3=map3.findViewById(R.id.bg_area);
        m1.setImageResource(R.drawable.bg_sea1);
        m2.setImageResource(R.drawable.bg_sea2);
        m3.setImageResource(R.drawable.bg_sea3);
        mapflipper.addView(map1,0);
        mapflipper.addView(map2,1);
        mapflipper.addView(map3,2);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden){
            netpingzi=null;
            netbitmap=null;
            nethouzhui=null;
            netspannablestring=null;
            netimgspan=null;
                heihei.dismiss();}
        super.onHiddenChanged(hidden);
    }
    class pingziview extends View {
        String pingziid;
        public pingziview(Context context) {
            super(context);
            setBackgroundResource(R.drawable.pingzi);
            setLayoutParams(new ViewGroup.LayoutParams(250,300));
        }
//        通过id判断
        public pingzi getpingzi() {
            pingzi re=null;
            for (pingzi q:
                 pingzilist) {
                if (q.getPingziid().equals(this.pingziid)){
                    re=q;
                    return q;
                }
            }
            return re;
        }
        public void setId(String id) {
            this.pingziid = id;
        }
        public String getPingziid(){
            return this.pingziid;
        }

    }


//    要补全网络获取到瓶子的图片显示和音乐播放（两者都是Uri形式）
    void createpingziview(){
        RelativeLayout r1,r2,r3;
        r1=map1.findViewById(R.id.pingzi_area);
        r2=map2.findViewById(R.id.pingzi_area);
        r3=map3.findViewById(R.id.pingzi_area);
        for (int i = 0; i < pingzilist.size(); i++) {
            pingziview x=new pingziview(getContext());
//            改成直接获取list项，list改成pingziidlist，由getfragpingzi产生
            x.setId(pingzilist.get(i).getPingziid());
            Random ranpos_x,ranpos_y,rnum;
            ranpos_x=new Random();
            ranpos_y=new Random();
            int posx,posy;
            rnum=new Random();
            pingziviewList.add(x);
            posx=ranpos_x.nextInt(800-270);
            posy=ranpos_y.nextInt(250);
            x.setX(posx);
            x.setY(posy);
            x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    此处打开瓶子，要远程请求瓶子文件以及资源，通过userid和pingziid可以获得，需要远程操作
                    p=x.getpingzi();
                    System.out.println(p.getCatagory());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                netpingzi=nettools.netmethods.urlgetpingzi(p.getUserid(),p.getPingziid());
                                nethouzhui=nettools.netmethods.getreshouzhui(p);
                                if (!nethouzhui.get(0).equals(" ")){
                                    netbitmap=nettools.netmethods.getImage(netpingzi.getUserid(),netpingzi.getPingziid(),nethouzhui.get(0));
                                }
                                createlisthandler.sendEmptyMessage('Y');
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
//                    想服务器请求瓶子id对应的音乐资源
//                    处理获取资源列表后，打开的关系     两个网络方法的操作
                    /*Handler openit=new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what){
                                case 951:
                                    List<String> houzhui= (List<String>) msg.obj;
                                    ImageSpan imgspan=null;
//                                    pingzi bottle=null;
                                    *//*try {
//                                        bottle=nettools.netmethods.urlgetpingzi(p.getUserid(),p.getPingziid());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }*//*
                                    if (!houzhui.get(0).equals(" ")){
                                        try {
                                            Bitmap img=nettools.netmethods.getImage(p.getUserid(),p.getPingziid(),houzhui.get(0));
                                            imgspan=new ImageSpan(img);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (!houzhui.get(1).equals(" ")){
                                        Playmusicurl_service.isPlaying=true;
                                        Intent playmusic=new Intent(getContext(), Playmusicurl_service.class);
                                        playmusic.putExtra("uri1",p.getUserid());
                                        playmusic.putExtra("uri2",p.getPingziid());
                                        playmusic.putExtra("uri3",houzhui.get(1));
                                        getActivity().startService(playmusic);
                                        System.out.println("192.168.31.85:11451/pingzi/"+p.getUserid()+"/"+p.getPingziid()+"/"+p.getPingziid()+houzhui.get(1));
                                    }
//                                    System.out.println(bottle.getPingzitext());
                                    break;
                                default:break;
                            }
                        }
                    };*/
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {pingzi bottle=null;
                                Message mag=new Message();
                                mag.what=951;
                               List<String> houzhui = nettools.netmethods.getreshouzhui(x.getpingzi());
                                bottle=nettools.netmethods.urlgetpingzi(p.getUserid(),p.getPingziid());
                                mag.obj=houzhui;
                               openit.sendMessage(mag);
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();*/


//                    以下内容很重要
                    /*Playmusicurl_service.isPlaying=true;
                    heihei.showAsDropDown(rootview);
                    Intent playmusic=new Intent(getContext(), Playmusicurl_service.class);
//                    想服务器请求瓶子id对应的音乐资源
                    playmusic.putExtra("uri",x.getPingziid());
//                    这里也想其他地方一样，展示瓶子的图文内容，一样要显示图片
                    SpannableString spantext=new SpannableString(x.getpingzi().getPingzitext());
                    pingzitext.setText(spantext);*/
// 这一行记得整回去
// getActivity().startService(playmusic);
                }
            });
            switch (rnum.nextInt(3)){
                case 0:
                    r1.addView(x);
                    break;
                case 1:
                    r2.addView(x);
                    break;
                case 2:
                    r3.addView(x);
                    break;
            }
        }
        for (pingziview a:
             pingziviewList) {
            registerForContextMenu(a);
        }
    }
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        temppingziview=(pingziview) v;
        if (temppingziview.getpingzi().getCatagory()==1){
            menu.add(0,pingzi_pick,0,"捞起");
            menu.add(0,pingzi_throw,0,"放归");
        }else if(temppingziview.getpingzi().getCatagory()==2) {
            menu.add(0,pingzi_throw,0,"放归");
        }else if (temppingziview.getpingzi().getCatagory()==3){
            menu.add(0,pingzi_accept,0,"接受");
            menu.add(0,pingzi_throw,0,"放归");
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.pingzi_topmenu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.pingzi_pickagain:
                /*for (int i = 0; i < pingziviewList.size(); i++) {
                    pingziviewList.get(i).setVisibility(View.GONE);
                    try {
//                        将状态还原回去
                        nettools.netmethods.pingziset1(pingziviewList.get(i).getPingziid());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                pingziviewList.clear();
                pingzilist.clear();
                getfragpingzi();*/
                for (int i = 0; i < pingziviewList.size(); i++) {
                    pingziviewList.get(i).setVisibility(View.GONE);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < pingziviewList.size(); i++) {
                           /* pingziviewList.get(i).setVisibility(View.GONE);*/
                            try {
//                        将状态还原回去
                                nettools.netmethods.pingziset1(pingziviewList.get(i).getPingziid());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        pingziviewList.clear();
                        pingzilist.clear();
                        createlisthandler.sendEmptyMessage('A');
                    }
                }).start();

                break;
            case R.id.pingzi_make:
                if (MainActivity.currentuser!=null){
                    Intent makeintent=new Intent(getContext(), pingzi_A_produce.class);
                    startActivity(makeintent);
                }
                else {
                    new wodeLoginDiago(getContext()).show();
                }

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case pingzi_pick:
//                捞起瓶子的操作，本地将瓶子以及文件存储起来，数据库建立对应条目，服务器端要完全删除瓶子记录以及相关文件，我的模块下可以直接读取表中数据，然后再次浮现瓶子，将瓶子文件保存在
//                用户id/saved/瓶子id文件夹下，同时文件夹下海保存图片音乐资源
                try {
                    if (MainActivity.currentuser==null){
                        new wodeLoginDiago(getContext()).show();
                    }else {
                        temppingziview.setVisibility(View.GONE);
                        p= temppingziview.getpingzi();
                        pickpingzi();
                        pingzilist.remove(pingziviewList.indexOf(temppingziview));
                        pingziviewList.remove(temppingziview);
                        pingzi_db.execSQL("insert into saved_pingzis values(?,1,?)",new String[]{
                        temppingziview.getPingziid(),MainActivity.currentuser.getUid().toString()
                        });
                        temppingziview=null;
//                        告知服务器删除文件和记录
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case pingzi_throw:
//                将瓶子从list中删除,不再显示瓶子
//                要通知服务器还原瓶子漂流状态
                temppingziview.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        nettools nt=new nettools(getContext());
                        try {
                            nettools.netmethods.pingziset1(temppingziview.pingziid);
                            System.out.println("此id+++"+temppingziview.getpingzi().getPingziid());
                            pingzilist.remove(pingziviewList.indexOf(temppingziview));
                            pingziviewList.remove(temppingziview);
                            temppingziview=null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case pingzi_accept:
                pingzi to_play=temppingziview.getpingzi();
                to_game=new Intent(getContext(), youxiplay.class);
                to_game.putExtra("mode",1);
                to_game.putExtra("author_id",to_play.getUserid());
                to_game.putExtra("song_name",to_play.getPingziid());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                           nethouzhui=nettools.netmethods.getreshouzhui(to_play);
                            createlisthandler.sendEmptyMessage(0X888);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    return  true;
    }
    void getfragpingzi(){
//        此处与服务器沟通，获取瓶子列表，同时要包含图片和音乐的Uri
//        只能获取pingzi文件了，相应的，点击的时候设置handler，等到资源文件加载完毕再去操作？
//        等等，有个大问题，加载的时候怎么吧资源和瓶子对应并且显示？还有之后的下载
//        麻了麻了，给自己挖了个天坑
//        将接受的数据进行objectinputstream读取为pingzi类，逐个添加到列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pingzilist=nettools.netmethods.getpingzilist();
                    System.out.println(pingzilist.get(0).getCatagory());
                    createlisthandler.sendEmptyMessage(111);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //        然后从通知服务器下载对应资源，并删除数据库对应条目，下载下来的文件就放在pingzifloder目录下就行，命名为pingziid
    void pickpingzi() throws IOException {
        String externaldir= getContext().getApplicationContext().getExternalFilesDir("")+"/lelazy";
        String usrdir,pzdir;
        File dir;
        usrdir= MainActivity.currentuser.getUid().toString();
        dir=new File(externaldir+"/saved/"+usrdir);
        pingzi savedpingzi=temppingziview.getpingzi();
        pzdir=savedpingzi.getPingziid();
        File pingzifloder=new File(dir+"/"+pzdir);
        pingzifloder.mkdirs();
        File pingziMain=new File(pingzifloder.getAbsolutePath()+"/"+pzdir+".pingzi");
        ObjectOutputStream op=new ObjectOutputStream(new FileOutputStream(pingziMain));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    netpingzi=nettools.netmethods.urlgetpingzi(p.getUserid(),p.getPingziid());
                    nethouzhui=nettools.netmethods.getreshouzhui(p);
                    if (!nethouzhui.get(0).equals(" ")){
                        netbitmap=nettools.netmethods.getImage(netpingzi.getUserid(),netpingzi.getPingziid(),nethouzhui.get(0));
                        File picfile=new File(pingzifloder.getAbsolutePath()+"/"+netpingzi.getPingziid()+nethouzhui.get(0));
                        FileOutputStream fos=new FileOutputStream(picfile);
                        if (nethouzhui.get(0).toLowerCase().equals("jpg")||nethouzhui.get(0).toLowerCase().equals("jpeg"))
                        {
                            netbitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                        }
                        if (nethouzhui.get(0).toLowerCase().equals("png")){
                            netbitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
                        }
                        else {
                            netbitmap.compress(Bitmap.CompressFormat.WEBP,100,fos);
                        }
                    }
                    File musicfile=new File(pingzifloder.getAbsolutePath()+"/"+netpingzi.getPingziid()+nethouzhui.get(1));
                    nettools.netmethods.urlgetmusic(musicfile,netpingzi.getUserid(),netpingzi.getPingziid(),nethouzhui.get(1));
                    op.writeObject(netpingzi);
                    op.close();
//                        然后通知服务器删除对应的条目以及资源
                    nettools.netmethods.delpingzi(netpingzi.getPingziid());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
