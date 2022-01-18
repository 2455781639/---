package me.kirkhorn.knut.android_sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import me.kirkhorn.knut.android_sudoku.data.DBManager;
import me.kirkhorn.knut.android_sudoku.data.databaseBean;

public class rank_listActivity extends AppCompatActivity {
    TextView tv1,tv2,tv3,tv4;
    ListView listView;
    private String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_list2);
        tv1 = findViewById(R.id.tv_01);
        tv2 = findViewById(R.id.tv_02);
        tv3 = findViewById(R.id.tv_03);
        tv4 = findViewById(R.id.tv_04);
        tv1.setText(R.string.Player_ID);
        tv2.setText(R.string.Name);
        tv3.setText(R.string.Time);
        tv4.setText(R.string.Difficulty);
        listView = findViewById(R.id.lv);

        listView.setAdapter(new MyAdapter(DBManager.queryAll(),this));
    }


    public void back(View view) {
        finish();
    }

    public void easy(View view) {
        listView.setAdapter(new MyAdapter(DBManager.queryDifficulty("easy"),this));
    }

    public void normal(View view) {
        listView.setAdapter(new MyAdapter(DBManager.queryDifficulty("normal"),this));
    }

    public void hard(View view) {
        listView.setAdapter(new MyAdapter(DBManager.queryDifficulty("hard"),this));
    }
}