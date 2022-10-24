package com.weilai.jigsawpuzzle.util.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weilai.jigsawpuzzle.DaoMaster;
import com.weilai.jigsawpuzzle.DaoSession;
import com.weilai.jigsawpuzzle.db.RecordInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class DaoTool {
    public static DaoSession sDaoSession;
    private static DaoMaster daoMaster;

    public static void init(Context ctx) {
        PuzzleOpenHelper helper = new PuzzleOpenHelper(ctx, "weilai.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        sDaoSession = daoMaster.newSession();
    }

    public static void insertRecord(RecordInfo recordInfo) {
        sDaoSession.getRecordInfoDao().insert(recordInfo);
    }
    public static void deleteRecord(RecordInfo recordInfo){
        sDaoSession.getRecordInfoDao().delete(recordInfo);
    }

    public static void clearDataBase() {
        DaoMaster.dropAllTables(daoMaster.getDatabase(), true);
        DaoMaster.createAllTables(daoMaster.getDatabase(), true);
    }

    public static Observable<List<RecordInfo>> obtainAllRecordInfo() {
        String sql = "select r._id, r.FILE_PATH, r.FILE_NAME ,r.TIME from RECORD_INFO r order by time desc";
        return Observable.create(new ObservableOnSubscribe<List<RecordInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<RecordInfo>> emitter) throws Exception {
                Cursor cursor = sDaoSession.getDatabase().rawQuery(sql, null);
                List<RecordInfo> recordInfos = new ArrayList<>();
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            RecordInfo recordInfo = new RecordInfo();
                            recordInfo.setId(cursor.getLong(0));
                            recordInfo.setFilePath(cursor.getString(1));
                            recordInfo.setFileName(cursor.getString(2));
                            recordInfo.setTime(cursor.getLong(3));
                            recordInfos.add(recordInfo);
                        } while (cursor.moveToNext());
                    }
                }
                emitter.onNext(recordInfos);
            }
        });


    }
}
