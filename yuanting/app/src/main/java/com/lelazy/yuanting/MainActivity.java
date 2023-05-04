package com.lelazy.yuanting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lelazy.yuanting.classes.nettools;
import com.lelazy.yuanting.classes.user;
import com.lelazy.yuanting.fragments.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int gray=R.color.gray;
    int white=R.color.white;
    fragpingzi fragpingzi;
    fragyouxi fragyouxi;
    fragxuexi fragxuexi;
    fragwode fragwode;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    LinearLayout bottompingzi,bottomxuexi,bottomyouxi,bottomwode;
    public static  user currentuser;
    SharedPreferences readuser;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission(this);
        setContentView(R.layout.activity_main);
        currentuser=null;
        readuser=getSharedPreferences("wodeloginDialog",MODE_PRIVATE);
        if (readuser.getBoolean("status",false)){
            currentuser=new user();
            currentuser.setUsername(readuser.getString("UserName",""));
            currentuser.setUid(Integer.valueOf(readuser.getString("UserId","-1")));
            currentuser.setPasswd(readuser.getString("UserPassword",""));
        }
        fragpingzi=new fragpingzi();
        fragyouxi=new fragyouxi();
        fragxuexi=new fragxuexi();
        fragwode=new fragwode();
        bottompingzi=findViewById(R.id.bottom_navi_pingzi);
        bottomxuexi=findViewById(R.id.bottom_navi_xuexi);
        bottomyouxi=findViewById(R.id.bottom_navi_youxi);
        bottomwode = findViewById(R.id.bottom_navi_wode);
        fragmentManager=getSupportFragmentManager();
        transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.frag_area,fragpingzi);
        transaction.commit();
        bottompingzi.setOnClickListener(this);
        bottomxuexi.setOnClickListener(this);
        bottomyouxi.setOnClickListener(this);
        bottomwode.setOnClickListener(this);
        bottompingzi.setBackgroundColor(gray);
        bottomxuexi.setBackgroundColor(white);
        bottomyouxi.setBackgroundColor(white);
        bottomwode.setBackgroundColor(white);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        hideFragment();
        switch (v.getId())
        {
            case R.id.bottom_navi_pingzi:
                showFragment(fragpingzi);
                v.setBackgroundColor(gray);
                bottomxuexi.setBackgroundColor(white);
                bottomyouxi.setBackgroundColor(white);
                bottomwode.setBackgroundColor(white);
                transaction.commit();
                break;
            case R.id.bottom_navi_xuexi:
                showFragment(fragxuexi);
                v.setBackgroundColor(gray);
                bottompingzi.setBackgroundColor(white);
                bottomyouxi.setBackgroundColor(white);
                bottomwode.setBackgroundColor(white);
                transaction.commit();
                break;
            case R.id.bottom_navi_youxi:
                showFragment(fragyouxi);
                v.setBackgroundColor(gray);
                bottomxuexi.setBackgroundColor(white);
                bottompingzi.setBackgroundColor(white);
                bottomwode.setBackgroundColor(white);
                transaction.commit();
                break;
            case R.id.bottom_navi_wode:
                showFragment(fragwode);
                v.setBackgroundColor(gray);
                bottomxuexi.setBackgroundColor(white);
                bottomyouxi.setBackgroundColor(white);
                bottompingzi.setBackgroundColor(white);
                transaction.commit();
                break;
        }
    }
    /**
     * 隐藏当前fragment,显示下一个fragment
     *
     * @param nextFragment
     */
    public void showFragment(Fragment nextFragment) {
        if (nextFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment  PreviousFragment = getSupportFragmentManager().findFragmentById(R.id.frag_area);//获取当前fragment
            if (PreviousFragment != null) {
                transaction.hide(PreviousFragment);
            }
            if (!nextFragment.isAdded()) {//判断是否以添加过
                transaction.add(R.id.frag_area, nextFragment);
                transaction.addToBackStack(null);//添加到回退栈  重要!!!
            }
            transaction.show(nextFragment).commit();
        }
    }
    /**
     * 返回上一个fragment
     */
    public void hideFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
    }
    //设备API大于6.0时，主动申请权限
    private void requestPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
            if (ContextCompat.checkSelfPermission(context,Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.INTERNET,
                        Manifest.permission.INTERNET}, 0);
            }
        }
    }
    /*@Override
    protected void onDestroy() {
        System.out.println(fragpingzi.pingzilist.get(0).getPingziid());
        try {
            nettools.netmethods.pingzilistset1(fragpingzi.pingzilist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }*/
}