package com.lelazy.yuanting.aboutWode;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.lelazy.yuanting.MainActivity;
import com.lelazy.yuanting.R;
import com.lelazy.yuanting.aboutPingzi.pingzicontent;
import com.lelazy.yuanting.classes.nettools;
import com.lelazy.yuanting.classes.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class wodeLoginDiago extends Dialog implements View.OnClickListener {
    public wodeLoginDiago(@NonNull Context context) {
        super(context);
    }

    public wodeLoginDiago(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected wodeLoginDiago(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    Button register,dialogin;
    CheckBox rememberpwd;
    EditText  wodedialogaccount,wodedialogpwd,wodedialogusername;
    TextView wodedialogforgetpwd;
    String accountPwd,accountName,accountId;
    SharedPreferences UserLogMessage;
    SharedPreferences.Editor UserLogMessEdit;
//    boolean logincode;
    Integer logincode=-1;
//这里要和服务器沟通，拿到登录用户的name信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode_logindialog);
        register=findViewById(R.id.wode_register);
        dialogin=findViewById(R.id.wode_login);
        rememberpwd=findViewById(R.id.wode_remember);
        wodedialogaccount=findViewById(R.id.wode_loginaccount);
        wodedialogusername=findViewById(R.id.wode_loginusername);
        wodedialogpwd=findViewById(R.id.wode_loginpassword);
        wodedialogforgetpwd=findViewById(R.id.wode_forgetpwd);
        UserLogMessage=getContext().getSharedPreferences("wodeloginDialog", Context.MODE_PRIVATE);
        UserLogMessEdit=UserLogMessage.edit();
        accountId=UserLogMessage.getString("UserId","");
        accountName=UserLogMessage.getString("UserName","");
        accountPwd=UserLogMessage.getString("UserPassword","");
        wodedialogaccount.setText(accountId);
        wodedialogusername.setText(accountName);
        wodedialogpwd.setText(accountPwd);
        register.setOnClickListener(this);
        dialogin.setOnClickListener(this);
        rememberpwd.setOnClickListener(this);
        wodedialogforgetpwd.setOnClickListener(this);
    }


    @Override
    public void show() {
        setCanceledOnTouchOutside(false);
        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wode_forgetpwd:
                Intent inten=new Intent(getContext(),wode_forgetpwd.class);
                getContext().startActivity(inten);
                break;
            case R.id.wode_login:
                accountId=wodedialogaccount.getText().toString();
                accountName=wodedialogusername.getText().toString();
                accountPwd=wodedialogpwd.getText().toString();
                if (!accountId.equals("")&&!accountName.equals("")&&!accountPwd.equals("")){
//                    此处与服务器沟通确认是否账号密码正确，返回用户的name，id和密码。并在之后进行写入
                    Map<String,String> param=new HashMap<>();
                    param.put("uid",accountId);
                    param.put("username",accountName);
                    param.put("pwd",accountPwd);
                    Handler xhand=new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {

                            switch (msg.what){
                                case 0X1:
                                    switch ((Integer)msg.obj){
                                        case 1:
//                                            logincode=1;
                                            MainActivity.currentuser=new user();
                                            MainActivity.currentuser.setUid(Integer.valueOf(accountId));
                                            MainActivity.currentuser.setUsername(accountName);
                                            MainActivity.currentuser.setPasswd(accountPwd);
                                            if (rememberpwd.isChecked()){
                                                UserLogMessEdit.putString("UserId",accountId);
                                                UserLogMessEdit.putString("UserName",accountName);
                                                UserLogMessEdit.putString("UserPassword",accountPwd);
                                            }
                                            UserLogMessEdit.putBoolean("status",true);
                                            UserLogMessEdit.commit();
                                            logincode=-1;
                                            dismiss();
                                            break;
                                        case 0:
//                                            logincode=0;
                                            new AlertDialog.Builder(getContext()).setMessage("输入错误，请重新输入")
                                                    .setPositiveButton("知晓", new OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    }).show();
                                            break;
                                        default:
                                            break;
                                    }
                                    super.handleMessage(msg);
                            }
                        }
                    };
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            nettools nt=new nettools(getContext());
                            try {
                                Integer result;
                                result=nettools.netmethods.login(param);
                                Message msg=new Message();
                                msg.what=0X1;
                                msg.obj=result;
                                xhand.sendMessage(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Looper.loop();
                        }
                    }).start();
                }
                else {
                    Toast.makeText(getContext(),"请完整输入",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.wode_register:
                Intent intent=new Intent(getContext(),wode_register.class);
                getContext().startActivity(intent);
                break;
        }
    }


}
