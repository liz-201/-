package com.lelazy.yuanting.aboutWode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class wode_forgetpwd extends AppCompatActivity {
    EditText updateid,updatename,updatepwd1,updatepwd2;
    Button updatenow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode_forgetpwd);
        updateid=findViewById(R.id.wode_update_userid);
        updatename=findViewById(R.id.wode_update_username);
        updatepwd1=findViewById(R.id.wode_update_pwd1);
        updatepwd2=findViewById(R.id.wode_update_pwd2);
        updatenow=findViewById(R.id.wode_update_bt);
        updatenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid,uname,pwd1,pwd2;
                uid=updateid.getText().toString();
                uname=updatename.getText().toString();
                pwd1=updatepwd1.getText().toString();
                pwd2=updatepwd2.getText().toString();
                if (uid.equals("")|pwd1.equals("")|pwd2.equals("")|uname.equals("")){
                    Toast.makeText(v.getContext(),"请完整输入",Toast.LENGTH_SHORT).show();
                }
                 else if (!pwd1.equals(pwd2)){
                    Toast.makeText(v.getContext(),
                            "请确保两次密码一致",
                            Toast.LENGTH_SHORT).show();
                }
                 else {
//                     与服务器沟通，修改完成后弹出弹窗通知
                    nettools nt=new nettools(wode_forgetpwd.this);
                    Integer intuid=Integer.valueOf(uid);
                    Handler xhandler=new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            switch (msg.what){
                                case 0X256:
                                    Integer rescode=(Integer) msg.obj;
                                    if (rescode.equals(1)){
                                        Toast.makeText(wode_forgetpwd.this,"修改成功，请牢记密码",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                            }
                            super.handleMessage(msg);
                        }
                    };
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Integer res=nettools.netmethods.changpwd(intuid,uname,pwd1);
                                Message msg=new Message();
                                msg.what=0X256;
                                msg.obj=res;
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