package com.weilai.jigsawpuzzle.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class RecordInfo {
    @Id(autoincrement = true)
    private Long id;
    private String fileName;
    private String filePath;
    private long time;
    @Generated(hash = 1145482985)
    public RecordInfo(Long id, String fileName, String filePath, long time) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.time = time;
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
}
