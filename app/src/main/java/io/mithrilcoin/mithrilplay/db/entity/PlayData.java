package io.mithrilcoin.mithrilplay.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Gamedata info DB
 */

@Entity(tableName = "playdata", indices = {@Index(value = {"packageName", "startTime", "endtime"}, unique = true)})
public class PlayData {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Integer idx;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "packageName")
    public String packageName;

    @ColumnInfo(name = "startTime")
    public String startTime;

    @ColumnInfo(name = "endtime")
    public String endtime;

    @ColumnInfo(name = "playTime")
    public String playTime;

    @ColumnInfo(name = "appVersion")
    public String appVersion;

    public PlayData(String email, String packageName, String startTime, String endtime, String playTime, String appVersion) {
        this.email = email;
        this.packageName = packageName;
        this.startTime = startTime;
        this.endtime = endtime;
        this.playTime = playTime;
        this.appVersion = appVersion;
    }

}
