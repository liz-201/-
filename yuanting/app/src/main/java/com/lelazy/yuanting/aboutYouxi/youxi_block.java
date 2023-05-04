package com.lelazy.yuanting.aboutYouxi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.lelazy.yuanting.R;

import java.util.Random;
import java.util.Timer;

public class youxi_block extends View {
    Context context;

    public youxi_block(Context context) {
        super(context);
        this.context=context;
        setBackgroundResource(R.drawable.youxi_block);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300));
    }
    void blockdes() throws Throwable {
        this.finalize();
    }

}
