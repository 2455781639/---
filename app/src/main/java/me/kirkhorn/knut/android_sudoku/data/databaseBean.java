package me.kirkhorn.knut.android_sudoku.data;

public class databaseBean {
    private int _id;
    private String name;
    private int time;
    private String broad;

    public String getBroad() {
        return broad;
    }

    public void setBroad(String broad) {
        this.broad = broad;
    }

    public databaseBean(){}

    public databaseBean(int _id, String name, int time,String broad) {
        this._id = _id;
        this.name = name;
        this.time = time;
        this.broad = broad;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
