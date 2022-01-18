package me.kirkhorn.knut.android_sudoku.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static SQLiteOpenHelper mInstance;

    public static SQLiteOpenHelper getInstance(Context context) {
        if(mInstance == null){
            mInstance = new DBHelper(context,"info.db",null,1);
        }
        return mInstance;
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        String sql = "create table sudoku(_id integer primary key autoincrement,name varchar(20) not null,time integer not null,broad varchar(20) not null)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
