package com.lelazy.yuanting.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lelazy.yuanting.MainActivity;
import com.lelazy.yuanting.aboutWode.*;
import com.lelazy.yuanting.R;

public class fragwode extends Fragment implements View.OnClickListener {
    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.page_wode, container);

        return root;
    }*/
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView scanpingzi,loginAndname,change,tick;
    LinearLayout login;
    SharedPreferences x;
    public fragwode() {
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
    public static fragwode newInstance(String param1, String param2) {
        fragwode fragment = new fragwode();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.page_wode, container, false);
        View root=inflater.inflate(R.layout.page_wode, container, false);
        scanpingzi=root.findViewById(R.id.scan_pingzi);
        loginAndname=root.findViewById(R.id.loginAndname);
        x=getContext().getSharedPreferences("wodeloginDialog", Context.MODE_PRIVATE);
        if (x.getBoolean("status",false)){
            loginAndname.setText(x.getString("UserName",""));
        }
        scanpingzi.setOnClickListener(this);
        loginAndname.setOnClickListener(this);
        change=root.findViewById(R.id.changemess);
        change.setOnClickListener(this);
        tick=root.findViewById(R.id.wode_tickmess);
        tick.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scan_pingzi:
                if (MainActivity.currentuser!=null){
                    Intent intent=new Intent(this.getContext(),scan_pingzi.class);
                    startActivity(intent);
                }
                else {
                    new wodeLoginDiago(getContext()).show();
                }
                break;
            case R.id.loginAndname:
                if (!x.getBoolean("status",false)){
                    new wodeLoginDiago(getContext()).show();
                    System.out.println(x.getBoolean("status",false));
                }
                break;
            case R.id.changemess:
                if (MainActivity.currentuser!=null){
                    Intent in=new Intent(getContext(),wode_change.class);
                    startActivity(in);
                    loginAndname.setText(x.getString("UserName",null));
//                    startActivityForResult(in,0X121);
                }
                else {
                    new wodeLoginDiago(getContext()).show();
                }
                break;
            case R.id.wode_tickmess:
                SharedPreferences.Editor y=x.edit();
                MainActivity.currentuser=null;
                y.putBoolean("status",false);
                y.commit();
                loginAndname.setText("请登录");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 0X121:
                if (requestCode==1){
                    Toast.makeText(getContext(),"修改成功",Toast.LENGTH_SHORT).show();
//                    更新信息的操作
                    break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
