package com.lelazy.yuanting.aboutWode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelazy.yuanting.R;
import com.lelazy.yuanting.classes.nettools;

import java.io.IOException;
import java.net.MalformedURLException;

public class wode_change extends AppCompatActivity {
    ImageView head;
    TextView name;
    EditText editname;
    Button changeOK;
    SharedPreferences x;
    SharedPreferences.Editor y;
    Integer uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode_change);
        head=findViewById(R.id.wode_changehead);
        name=findViewById(R.id.wode_change_name);
        editname=findViewById(R.id.wode_changename);
        changeOK=findViewById(R.id.changemess_OK);
        x=getSharedPreferences("wodeloginDialog", Context.MODE_PRIVATE);
        y=x.edit();
        editname.setText(x.getString("UserName",null));
        uid= Integer.valueOf(x.getString("UserId",null));
        changeOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        进行修改，并返回结果
                nettools nt=new nettools(wode_change.this);
                String newname=editname.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            nettools.netmethods.changename(uid,newname);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                y.putString("UserName",newname);
                y.commit();
                finish();
//                onActivityResult(0X121,1,null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0X121:
                new Thread(new Runnable() {
                    String newname=editname.getText().toString();
                    @Override
                    public void run() {
                        try {
                            nettools.netmethods.changename(uid,newname);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                resultCode=1;
                finish();
                break;
        }
    }
}