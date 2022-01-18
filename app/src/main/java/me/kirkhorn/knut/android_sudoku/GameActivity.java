package me.kirkhorn.knut.android_sudoku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import me.kirkhorn.knut.android_sudoku.data.DBManager;
import me.kirkhorn.knut.android_sudoku.fragments.CellGroupFragment;
import me.kirkhorn.knut.android_sudoku.model.Board;

/**
 * Created by Knut on 19.11.2017.
 */

public class GameActivity extends AppCompatActivity implements CellGroupFragment.OnFragmentInteractionListener {
    private final String TAG = "GameActivity";
    private TextView clickedCell;
    private int clickedGroup;
    private int clickedCellId;
    private Board startBoard;
    private Board currentBoard;
    private TextView tv_time;
    private int recLen = 0;//时间
    private String broad;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //记录时间
        tv_time = findViewById(R.id.tv_time);
        handler.postDelayed(runnable, 1000);

        int difficulty = getIntent().getIntExtra("difficulty", 0);
        ArrayList<Board> boards = readGameBoards(difficulty);
        //从本地已经内置得数独和新建的数独文件中随机挑选出一个数独供玩家开始挑战
        startBoard = chooseRandomBoard(boards);
        currentBoard = new Board();
        currentBoard.copyValues(startBoard.getGameCells());

        int[] cellGroupFragments = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3, R.id.cellGroupFragment4,
                R.id.cellGroupFragment5, R.id.cellGroupFragment6, R.id.cellGroupFragment7, R.id.cellGroupFragment8, R.id.cellGroupFragment9};
        for (int i = 1; i < 10; i++) {
            CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getSupportFragmentManager().findFragmentById(cellGroupFragments[i-1]);
            if (thisCellGroupFragment != null) {
                thisCellGroupFragment.setGroupId(i);
            }
        }

        //Appear all values from the current board
        CellGroupFragment tempCellGroupFragment;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int column = j / 3;
                int row = i / 3;

                int fragmentNumber = (row * 3) + column;
                tempCellGroupFragment = (CellGroupFragment) getSupportFragmentManager().findFragmentById(cellGroupFragments[fragmentNumber]);
                int groupColumn = j % 3;
                int groupRow = i % 3;

                int groupPosition = (groupRow * 3) + groupColumn;
                int currentValue = currentBoard.getValue(i, j);

                if (currentValue != 0) {
                    if (tempCellGroupFragment != null) {
                        tempCellGroupFragment.setValue(groupPosition, currentValue);
                    }
                }
            }
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen++;
            tv_time.setText(String.format(Locale.CHINA,"%02d",recLen/60)+":"+String.format(Locale.CHINA,"%02d",recLen%60));
            handler.postDelayed(this, 1000);
        }
    };


    //读取已经确定的数独盘文件，建立数独
    //或者从已经新建立得文件中提取新建得数独然后开始游戏
    private ArrayList<Board> readGameBoards(int difficulty) {
        ArrayList<Board> boards = new ArrayList<>();
        int fileId;
        if (difficulty == 1) {
            fileId = R.raw.normal;
        } else if (difficulty == 0) {
            fileId = R.raw.easy;
        } else {
            fileId = R.raw.hard;
        }

        InputStream inputStream = getResources().openRawResource(fileId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                Board board = new Board();
                // read all lines in the board
                for (int i = 0; i < 9; i++) {
                    String[] rowCells = line.split(" ");
                    for (int j = 0; j < 9; j++) {
                        if (rowCells[j].equals("-")) {
                            board.setValue(i, j, 0);
                        } else {
                            board.setValue(i, j, Integer.parseInt(rowCells[j]));
                        }
                    }
                    line = bufferedReader.readLine();
                }
                boards.add(board);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        //reading from internal storage (/data/data/<package-name>/files)
        String fileName = "boards-";
        if (difficulty == 0) {
            fileName += "easy";
            broad = "easy";
        } else if (difficulty == 1) {
            fileName += "normal";
            broad = "normal";
        } else {
            fileName += "hard";
            broad = "hard";
        }
        FileInputStream fileInputStream;
        try {
            fileInputStream = this.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader internalBufferedReader = new BufferedReader(inputStreamReader);
            String line = internalBufferedReader.readLine();
            line = internalBufferedReader.readLine();
            while (line != null) {
                Board board = new Board();
                // read all lines in the board
                for (int i = 0; i < 9; i++) {
                    String[] rowCells = line.split(" ");
                    for (int j = 0; j < 9; j++) {
                        if (rowCells[j].equals("-")) {
                            board.setValue(i, j, 0);
                        } else {
                            board.setValue(i, j, Integer.parseInt(rowCells[j]));
                        }
                    }
                    line = internalBufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                }
                boards.add(board);
                line = internalBufferedReader.readLine();
            }
            internalBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boards;
    }

    private Board chooseRandomBoard(ArrayList<Board> boards) {
        int randomNumber = (int) (Math.random() * boards.size());
        return boards.get(randomNumber);
    }

    //group为第几个大九宫格，cell为九宫格里面的第几个九宫格
    //然后row和column可以确定该位置得坐标通过二维数组数独盘确定它是不是空
    private boolean isStartPiece(int group, int cell) {
        int row = ((group-1)/3)*3 + (cell/3);
        int column = ((group-1)%3)*3 + ((cell)%3);
        return startBoard.getValue(row, column) != 0;
    }

    //检查每一个小的九宫格是否有重复数据
    private boolean checkAllGroups() {
        int[] cellGroupFragments = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3, R.id.cellGroupFragment4,
                R.id.cellGroupFragment5, R.id.cellGroupFragment6, R.id.cellGroupFragment7, R.id.cellGroupFragment8, R.id.cellGroupFragment9};
        for (int i = 0; i < 9; i++) {
            CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getSupportFragmentManager().findFragmentById(cellGroupFragments[i]);
            if (!thisCellGroupFragment.checkGroupCorrect()) {
                return false;
            }
        }
        return true;
    }

    //完成数独盘后还是判断是否通过
    public void onCheckBoardButtonClicked(View view) {
        currentBoard.isBoardCorrect();
        if(checkAllGroups() && currentBoard.isBoardCorrect()) {
            //每一个小的九宫格和每一行每一列都不能有重复数据
            //这样才是一个successful
            Toast.makeText(this, getString(R.string.board_correct), Toast.LENGTH_SHORT).show();
            //代码至此，判断数独通过
            EditText edt = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("请输入姓名")
                    .setIcon(R.drawable.tubiao)
                    .setView(edt)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = edt.getText().toString();
                            DBManager.add(name,recLen,broad);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
//            Intent intent = new Intent(this,MainActivity.class);
//            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.board_incorrect), Toast.LENGTH_SHORT).show();
        }
    }

    //退出
    public void onGoBackButtonClicked(View view) {
        finish();
    }

    //显示提示
    public void onShowInstructionsButtonClicked(View view) {
        Intent intent = new Intent("me.kirkhorn.knut.InstructionsActivity");
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)//消除高版本Api在低版本SDK上的报错
    @Override//移除，选择数字，不确定都会在这个执行
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int row = ((clickedGroup - 1) / 3) * 3 + (clickedCellId / 3);
            int column = ((clickedGroup - 1) % 3) * 3 + ((clickedCellId) % 3);

            Button buttonCheckBoard = findViewById(R.id.buttonCheckBoard);
            if (data.getBooleanExtra("removePiece", false)) {
                clickedCell.setText("");
                clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell));
                currentBoard.setValue(row, column, 0);
                buttonCheckBoard.setVisibility(View.INVISIBLE);
            } else {
                int number = data.getIntExtra("chosenNumber", 1);
                //插入选中的数字
                clickedCell.setText(String.valueOf(number));
                //设置插入字体大小
                clickedCell.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                currentBoard.setValue(row, column, number);

                //isUnsure->棋盘中某格子的不确定
                boolean isUnsure = data.getBooleanExtra("isUnsure", false);
                if (isUnsure) {
                    clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell_unsure));
                } else {
                    clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell));
                }

                //isBoardFul()->判断棋盘已满
                if (currentBoard.isBoardFull()) {
                    //检验button可见
                    buttonCheckBoard.setVisibility(View.VISIBLE);
                } else {
                    //检验button不可见
                    buttonCheckBoard.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    //点击某一空格即调用此方法，用于定位此格子的位置
    @Override
    public void onFragmentInteraction(int groupId, int cellId, View view) {
        clickedCell = (TextView) view;
        clickedGroup = groupId;
        clickedCellId = cellId;
        Log.i(TAG, "Clicked group " + groupId + ", cell " + cellId);

        //isStartPiece(groupId, cellId)若为true则说明该各自!=0->该格子不为空
        if (!isStartPiece(groupId, cellId)) {
            Intent intent = new Intent("me.kirkhorn.knut.ChooseNumberActivity");
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(this, getString(R.string.start_piece_error), Toast.LENGTH_SHORT).show();
        }
    }
}
