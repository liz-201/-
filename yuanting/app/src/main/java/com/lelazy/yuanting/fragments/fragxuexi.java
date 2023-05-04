package com.lelazy.yuanting.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lelazy.yuanting.MainActivity;
import com.lelazy.yuanting.R;
import com.lelazy.yuanting.aboutWode.wodeLoginDiago;
import com.lelazy.yuanting.aboutXuexi.xuexi_section;
import com.lelazy.yuanting.aboutXuexi.xuexi_video;
import com.lelazy.yuanting.classes.nettools;
import com.lelazy.yuanting.aboutXuexi.pingzi_B_produce;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragxuexi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragxuexi extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View sections;/*
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;*/
    ListView sectionlist;
//    课程分类
    String[] course_category_names;
    Fragment sectionfrag;
//    课程集数
    ArrayList<String> course_sections;

    GridLayout area;
    AutoCompleteTextView searchtext;
    GridLayout gridLayout;
    List<Map<String,Integer>> courses;
    List<String> catagorylist;
    List<Integer> jishulist;
    ListAdapter adapter;
    ArrayAdapter<String> xx;
    List<Map<String,String[]>> itemssss;
    List<String> liststring;
    String temp_jishu;
    String temp_course;
    Handler xhandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0X789:
                    adapter=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,catagorylist);
                    sectionlist.setAdapter(adapter);
                    itemssss=new ArrayList<>();
                    liststring=new ArrayList<>();
                    for (int i = 0; i < catagorylist.size(); i++) {
                        for (int j = 0; j < jishulist.get(i); j++) {
                            String keyji=catagorylist.get(i)+"第"+(j+1)+"集";
                            liststring.add(keyji);
                            String[] shuzu=new String[]{
                                    catagorylist.get(i), String.valueOf(j)
                            };
                            Map<String,String[]> mm=new HashMap<>();
                            mm.put(keyji,shuzu);
                            itemssss.add(mm);
                        }
                    }
                    xx=new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,liststring);
                    searchtext.setAdapter(xx);
                    System.out.println("集数：：：：：：：：：：：：：：：："+jishulist);
                    System.out.println(catagorylist);
                    break;
                default:break;
            }
            super.handleMessage(msg);
        }
    };
    public fragxuexi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragxuexi.
     */
    // TODO: Rename and change types and number of parameters
    public static fragxuexi newInstance(String param1, String param2) {
        fragxuexi fragment = new fragxuexi();
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
//      沟通服务器，获取分类
        nettools nt=new nettools(getContext());
        courses=new ArrayList<>();
        catagorylist=new ArrayList<>();
        jishulist=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    courses=nettools.netmethods.getcoursemess();
                    for (int i = 0; i < courses.size(); i++) {
                        Iterator<String> it=courses.get(i).keySet().iterator();
                        catagorylist.add(it.next());
                        jishulist.add(courses.get(i).get(catagorylist.get(i)));
                    }
                    xhandler.sendEmptyMessage(0X789);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.page_xuexi, container, false);
        sectionlist=root.findViewById(R.id.xuexi_courselist);
        searchtext=root.findViewById(R.id.xuexi_searchtext);
        searchtext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cvcv= parent.getItemAtPosition(position).toString();
                Intent tovideo=new Intent(getContext(),xuexi_video.class);
                Integer num=liststring.indexOf(cvcv);
                String[] shuzu=itemssss.get(num).get(cvcv);
                tovideo.putExtra("name",shuzu[0]);
                tovideo.putExtra("jishu",shuzu[1]);
                startActivity(tovideo);
            }
        });
        View fraglayout=inflater.inflate(R.layout.fragment_xuexi_section,container,false);
        area=root.findViewById(R.id.xuexi_courses_area);
        gridLayout=fraglayout.findViewById(R.id.section_grid);
        sectionlist.setAdapter(adapter);
        sections=root.findViewById(R.id.xuexi_courses_area);
        sectionfrag=new xuexi_section();
        showFragment(sectionfrag);
        sectionlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),course_category_names[position],Toast.LENGTH_SHORT).show();
                area.removeAllViews();
//                hideFragment();
                for (int i = 0; i < jishulist.get(position); i++) {
                    Button bt=new Button(getContext());
                    bt.setText(String.valueOf(i+1));
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent inn=new Intent(getContext(),xuexi_video.class);
                            Integer jishu= Integer.valueOf(bt.getText().toString())-1;
                            inn.putExtra("name",catagorylist.get(position));
                            inn.putExtra("jishu",/*jishulist.get(position)*/jishu.toString());
                            startActivity(inn);
                        }
                    });
                    registerForContextMenu(bt);
                    area.addView(bt);
                    temp_course=catagorylist.get(position);
                }
                /*nettools nt=new nettools(getContext());
                Handler xhandler=new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        switch (msg.what){
                            case 0X963:
                                List<Map<String,Integer>> inn= (List<Map<String, Integer>>) msg.obj;
                                for (int i = 0; i < inn.size(); i++) {
                                    Button bt=new Button(getContext());
                                    bt.setText(String.valueOf(i));
                                    bt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //                根据点击结果，生成数据传给视频播放的activity，然后联系服务器远程播放
                                            Log.d("点击的是", course_category_names[position]+":"+bt.getText());
                                            Intent intent=new Intent(getContext(), xuexi_video.class);
                                            intent.putExtra("catagory",course_category_names[position]);
                                            intent.putExtra("section",bt.getText());
                                            startActivity(intent);
                                        }
                                    });
                                    area.addView(bt);
                                }
                                break;
                        }
                        super.handleMessage(msg);
                    }
                };*/
//                course_sections=new ArrayList<>();
                /*course_sections.add("xfg");
                course_sections.add("fcg");
                course_sections.add("jikl");*/
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            courses=nettools.netmethods.getcoursemess();
                            Message msg=new Message();
                            msg.what=0X963;
                            msg.obj=courses;
                            System.out.println(courses.get(1));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();*/
//                查询电机的课程多少课，fragment生成课程的集数按钮，点击跳转对应界面播放
//                showFragment(new xuexi_section());
//根据服务器的数据生成按钮列表，点击后获取列表的第几项（分类），第几节课，然后将两个数据传过去，用intent传过去，进行视频播放
                /*for (int i = 0; i < 7; i++) {
                    Button bt=new Button(getContext());
                    bt.setText(String.valueOf(i));
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //                根据点击结果，生成数据传给视频播放的activity，然后联系服务器远程播放
                            Log.d("点击的是", course_category_names[position]+":"+bt.getText());
                            Intent intent=new Intent(getContext(), xuexi_video.class);
                            intent.putExtra("catagory",course_category_names[position]);
                            intent.putExtra("section",bt.getText());
                            startActivity(intent);
                        }
                    });
                    area.addView(bt);
                }*/
//                showFragment(new xuexi_section());
//                fragmentTransaction.commit();
//                Toast.makeText(getContext(),course_sections.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
    public void showFragment(Fragment nextFragment) {
        if (nextFragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment  PreviousFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.xuexi_courses_area);//获取当前fragment
            if (PreviousFragment != null) {
                transaction.hide(PreviousFragment);
            }
            if (!nextFragment.isAdded()) {//判断是否以添加过
                transaction.add(R.id.xuexi_courses_area, nextFragment);
                transaction.addToBackStack(null);//添加到回退栈  重要!!!
            }
            transaction.show(nextFragment).commit();
        }
    }
    /**
     * 返回上一个fragment
     */
    public void hideFragment() {
        FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,1,0,"吐槽");
        temp_jishu=((Button)v).getText().toString();
        System.out.println(temp_jishu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (MainActivity.currentuser==null){
            new wodeLoginDiago(getContext()).show();
        }
        else {
            Intent to_make_B=new Intent(getContext(),pingzi_B_produce.class);
            System.out.println(temp_course);
            to_make_B.putExtra("course_name",temp_course);
            to_make_B.putExtra("course_section",temp_jishu);
            startActivity(to_make_B);
        }
        return super.onContextItemSelected(item);
    }
}