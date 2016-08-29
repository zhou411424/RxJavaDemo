package com.xy.rxjava.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingyun on 2016/8/29.
 */
public class User {
    public User() {

    }

    public User(String name) {
        this.name = name;
    }

    public String name;

    public List<ScoreTip> scoreTipList = new ArrayList<>();
}
