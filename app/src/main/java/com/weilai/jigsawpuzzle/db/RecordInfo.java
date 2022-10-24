package com.weilai.jigsawpuzzle.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class RecordInfo {
    @Id(autoincrement = true)
    private Long id;
    private int position;
    private String fileName;
    private String filePath;
    private long time;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Generated(hash = 1309801334)
    public RecordInfo(Long id, int position, String fileName, String filePath,
            long time, boolean isSelected) {
        this.id = id;
        this.position = position;
        this.fileName = fileName;
        this.filePath = filePath;
        this.time = time;
        this.isSelected = isSelected;
    }

    @Generated(hash = 1863816245)
    public RecordInfo() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
