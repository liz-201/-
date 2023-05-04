package com.lelazy.yuanting.aboutWode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lelazy.yuanting.R;
import com.lelazy.yuanting.classes.nettools;

import java.io.IOException;

public class wode_register extends AppCompatActivity  {
    EditText username,pwd1,pwd2;
    Button register;
    Integer uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode_register);
        username=findViewById(R.id.wode_register_uername);
        pwd1=findViewById(R.id.wode_register_pwd1);
        pwd2=findViewById(R.id.wode_register_pwd2);
        register=findViewById(R.id.wode_register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pd1=pwd1.getText().toString();
                String pd2=pwd2.getText().toString();
                String name=username.getText().toString();
                if (!pd1.equals(pd2)){
                    Toast.makeText(v.getContext(),
                            "请确保两次密码一致",
                            Toast.LENGTH_SHORT).show();
                }
                else if (username.equals("")|pd1.equals("")|pd2.equals("")){
                    Toast.makeText(v.getContext(),"请勿留空",Toast.LENGTH_SHORT).show();
                }

                else {
                    //                实现注册,跳转回去,填入账号密码
                    Handler xhandler=new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            switch (msg.what){
                                case 0X256:
                                    Integer wakuwaku= (Integer) msg.obj;
                                    if (!wakuwaku.equals(-1)){
                                        uid=wakuwaku;
                                        new AlertDialog.Builder(wode_register.this)
                                                .setMessage("请牢记uid："+uid)
                                                .setPositiveButton("知晓", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                })
                                                .show();
                                    }
                                    else {
                                        new AlertDialog.Builder(wode_register.this)
                                                .setMessage("出现错误")
                                                .setPositiveButton("知晓", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                })
                                                .show();
                                    }
                            }
                            super.handleMessage(msg);
                        }
                    };
//                    要弹出一个对话框提醒用户账号是多少
                    nettools nt=new nettools(wode_register.this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg=new Message();
                            msg.what=0X256;
                            try {
                                Integer zawa;
                                zawa=nettools.netmethods.signup(name,pd1);
                                msg.obj=zawa;
                                xhandler.sendMessage(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

}