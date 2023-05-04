package com.lelazy.yuanting.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lelazy.yuanting.MainActivity;
import com.lelazy.yuanting.R;
import com.lelazy.yuanting.aboutYouxi.youxiplay;
import com.lelazy.yuanting.classes.Databasetool;
import com.lelazy.yuanting.classes.pingzi;
import com.lelazy.yuanting.classes.pingzireadstream;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragyouxi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragyouxi extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button youxiPlay;
    SharedPreferences gamepointreader;
    String bg_music;
    ListView bg_music_list;
    TextView maxpoint;
    ListAdapter bg_list_adpter;
    Cursor cursor;
    SQLiteDatabase pingzidb;
    Databasetool databasetool;
    ArrayList<String> bglist;
    public fragyouxi() {
        // Required empty public constructor
    }
    String song_name=" ";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragyouxi.
     */
    // TODO: Rename and change types and number of parameters
    public static fragyouxi newInstance(String param1, String param2) {
        fragyouxi fragment = new fragyouxi();
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
        gamepointreader=getContext().getSharedPreferences("Gamepoint", Context.MODE_PRIVATE);
        bglist=new ArrayList<>();
        databasetool=new Databasetool(getContext(),getContext().getFilesDir().getPath()+"/pingzi_saved.db",null,1);
        pingzidb=databasetool.getReadableDatabase();
//        cursor= pingzidb.query("saved_pingzis",null,null,null,null,null,null);
        cursor= pingzidb.query("saved_pingzis",new String[]{"*"},"master_id=?",new String[]{String.valueOf(MainActivity.currentuser.getUid())},
                null,null,null);
        if (cursor.moveToFirst()){
            do {
                bglist.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        bg_list_adpter=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,bglist );
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onSaveInstanceState(savedInstanceState);
        View root=inflater.inflate(R.layout.youxi_beforestart,container,false);
        youxiPlay=root.findViewById(R.id.youxi_beforePlay_play);
        maxpoint=root.findViewById(R.id.youxi_beforePlay_topscore);
        bg_music_list=root.findViewById(R.id.youxi_choose_bgmusic);
        SharedPreferences userreader= getActivity().getSharedPreferences("wodeloginDialog",Context.MODE_PRIVATE);
        bg_music_list.setAdapter(bg_list_adpter);
        if (userreader.getBoolean("status",false)){
            int max=gamepointreader.getInt("maxgamepoint",0);
            maxpoint.setText("最高分："+String.valueOf(max));
        }
        bg_music_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                song_name=bglist.get(position);
            }
        });
        youxiPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), youxiplay.class);
                if (!song_name.equals(" ")){
                    intent.putExtra("mode",0);
                    intent.putExtra("song_name",song_name);
                    song_name=" ";
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(),"选一首歌吧",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return root;
    }
}