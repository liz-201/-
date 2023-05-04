package com.lelazy.yuanting.aboutXuexi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.lelazy.yuanting.R;

public class xuexi_video extends AppCompatActivity {
    VideoView videoView;
    MediaController mediaController;
    String course_catagory,course_section;
    Uri videouri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuexi_video);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        course_catagory=getIntent().getStringExtra("name");
        course_section=getIntent().getStringExtra("jishu");
        Integer jishu=Integer.valueOf(course_section)+1;
        videoView=findViewById(R.id.xuexi_videoview);
        System.out.println(course_catagory+",,,,,,,,,,,,,,,,,,,"+jishu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                //设置为填充父窗体
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //设置布局
        videoView.setLayoutParams(layoutParams);
        mediaController=new MediaController(this);
//        构造uri，主机名端口名都是服务器，路径为：主机：端口/分类/集数
//        不过具体的实现还是要等服务器实现后再做决定
//        videoView.setVideoURI(Uri.parse("https://oss.zhiyu.art/sucai/preview/7c4c64bd5d69b57633f3611406ae068ca.mp4"));
        String uu="http://"+getString(R.string.host_ip)+":"+getString(R.string.host_port)+"/video/"+course_catagory+"/"+jishu+".mp4";
        System.out.println("视频资源这里找"+uu);
        videoView.setVideoURI(Uri.parse(uu));
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        videoView.requestFocus();
        videoView.start();
    }
}