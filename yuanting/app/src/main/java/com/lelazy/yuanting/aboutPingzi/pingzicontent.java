package com.lelazy.yuanting.aboutPingzi;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lelazy.yuanting.R;

public class pingzicontent extends PopupWindow {

    LayoutInflater inflater;
    View contentui;
    public pingzicontent(Context context) {
        super(context);
        this.inflater=LayoutInflater.from(context);
        this.contentui=inflater.inflate(R.layout.pingzi_content_popwindow,null,false);
    }
}
