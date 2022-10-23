package com.weilai.jigsawpuzzle.event;

import com.weilai.jigsawpuzzle.bean.PicInfo;

import java.util.List;

/**
 ** DATE: 2022/10/10
 ** Author:tangerine
 ** Description:
 **/
public class LpSplitEvent {
    public List<String> data;
    public int type;
    public LpSplitEvent (List<String> data, int type){
        this.data = data;
        this.type = type;
    }
}
