package com.yurtcu.operareddittask.model;

/**
 * Created by Baris on 04.02.2017.
 */

public class Response {
    private String kind;
    private OutmostData data = new OutmostData();

    public OutmostData getData() {
        return data;
    }
}
