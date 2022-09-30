package com.weilai.jigsawpuzzle.event;


import java.util.List;

public class LpSortEvent {
    public List<String> data;
    public LpSortEvent (List<String> data){
        this.data = data;
    }
}
