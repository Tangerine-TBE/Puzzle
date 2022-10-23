package com.weilai.jigsawpuzzle.event;


import com.weilai.jigsawpuzzle.bean.PicInfo;

import java.util.List;

public class LpSortEvent {
    public List<PicInfo> data;
    public LpSortEvent (List<PicInfo> data){
        this.data = data;
    }
}
