package me.kirkhorn.knut.android_sudoku.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Knut on 22.11.2017.
 */

public class Board {
    private final int[][] gameCells = new int[9][9];

    public Board() {

    }

    public void setValue(int row, int column, int value) {
        gameCells[row][column] = value;
    }

    public int[][] getGameCells() {
        return gameCells;
    }

    public void copyValues(int[][] newGameCells) {
        for (int i = 0; i < newGameCells.length; i++) {
            System.arraycopy(newGameCells[i], 0, gameCells[i], 0, newGameCells[i].length);
        }
    }

    public boolean isBoardFull() {
        //检查格子是否填满
        for (int[] gameCell : gameCells) {
            for (int i : gameCell) {
                if (i == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isBoardCorrect() {
        //检查横排
        for (int[] gameCell : gameCells) {
            ArrayList<Integer> numbers = new ArrayList<>();
            for (int number : gameCell) {
                if (numbers.contains(number)) {
                    return false;
                } else {
                    numbers.add(number);
                }
            }
        }
        //检查竖排
        for (int i = 0; i < gameCells.length; i++) {
            ArrayList<Integer> numbers = new ArrayList<>();
            for (int j = 0; j < gameCells[i].length; j++) {
                int number = gameCells[j][i];
                if (numbers.contains(number)) {
                    return false;
                } else {
                    numbers.add(number);
                }
            }
        }
        return true;
    }

    public int getValue(int row, int column) {
        return gameCells[row][column];
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();
        for (int[] gameCell : gameCells) {
            for (int j = 0; j < gameCell.length; j++) {
                if (j == 0) {
                    temp.append("\n");
                }

                int currentNumber = gameCell[j];
                if (currentNumber == 0) {//数字为0即为空
                    temp.append("-");
                } else {
                    temp.append(currentNumber);
                }

                if (j != (gameCell.length - 1)) {
                    temp.append(" ");
                }
            }
        }
        return temp.toString();
    }
}
