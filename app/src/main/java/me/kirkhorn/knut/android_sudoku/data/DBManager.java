package me.kirkhorn.knut.android_sudoku.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBManager{
    public static SQLiteOpenHelper helper;
    public static SQLiteDatabase database;
    public static void initDB(Context context){
        helper = DBHelper.getInstance(context);
        database = helper.getWritableDatabase();
    }

    public static List<databaseBean> queryAll(){
        Cursor cursor = database.query("sudoku",null,null,null,null,null,"time asc");
        List<databaseBean> list = new ArrayList<>();
        while(cursor.moveToNext()){
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int time = cursor.getInt(cursor.getColumnIndex("time"));
            String broad = cursor.getString(cursor.getColumnIndex("broad"));
            databaseBean databaseBean = new databaseBean(_id,name,time,broad);
            list.add(databaseBean);
        }
        return list;
    }

    public static void add(String name,int time,String broad){
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("time",time);
        values.put("broad",broad);
        database.insert("sudoku",null,values);
    }

    /* 删除表当中所有的数据信息*/
    public static void deleteAll(){
        String sql = "delete from sudoku";
        database.execSQL(sql);
    }


//    模糊查询不同难度时间升序查询
    public static List<databaseBean> queryDifficulty(String broad1){
        Cursor cursor = database.query("sudoku",null,"broad=?",new String[]{broad1},null,null,"time asc");
        List<databaseBean> list = new ArrayList<>();
        while(cursor.moveToNext()){
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int time = cursor.getInt(cursor.getColumnIndex("time"));
            String broad = cursor.getString(cursor.getColumnIndex("broad"));
            databaseBean databaseBean = new databaseBean(_id,name,time,broad);
            list.add(databaseBean);
        }
        return list;
    }


}
