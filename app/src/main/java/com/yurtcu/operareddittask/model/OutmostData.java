package com.yurtcu.operareddittask.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baris on 05.02.2017.
 */

public class OutmostData {
    private String modhash;
    private List<Child> children = new ArrayList<Child>();

    public List<Child> getChildren() {
        return children;
    }
}
