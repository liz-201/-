package com.lelazy.yuanting.aboutYouxi;

import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import java.util.Random;

public class youxiRun implements Runnable{
    boolean exit=false;
    boolean pause=false;
    int delaytime=1500;
    Handler hand;
    Message message;
    public youxiRun(Handler handler) {
        this.hand =handler;
        this.message=new Message();
    }
    @Override
    public void run() {

        while (!exit){
            while (pause){
            }
            Random wayrand=new Random();
            Integer ran=wayrand.nextInt(4)+1;
            Message idms=new Message();
            idms.what=0X101;//101,代表生成一个块对象，传递到主线程，让他生成并显示到界面
            idms.obj=ran;
            hand.sendMessage(idms);
            try {
                Thread.sleep(delaytime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void setExit(boolean b){
        this.exit=b;
    }
    public void setPause(boolean pause) {
        this.pause = pause;
    }
}
