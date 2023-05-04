package com.lelazy.yuanting.classes;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Databasetool extends SQLiteOpenHelper {
    Context mcontext;
    String create_pingzis="CREATE TABLE saved_pingzis(pingzi_id VARCHAR(60),pingzi_catagory int,master_id int)";
    public Databasetool(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mcontext=context;

    }
    public Databasetool(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.mcontext=context;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_pingzis);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
