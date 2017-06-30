package com.example.biro.scm.DataBase;

import android.database.sqlite.SQLiteOpenHelper;

import com.clough.android.androiddbviewer.ADBVApplication;
import com.example.biro.scm.DataBase.DataBaseManger;


/**
 * Created by Biro on 6/18/2017.
 */

public class CustomApplication extends ADBVApplication {
    @Override
    public SQLiteOpenHelper getDataBase() {
        return DataBaseManger.getInstance(getApplicationContext());
    }
}
