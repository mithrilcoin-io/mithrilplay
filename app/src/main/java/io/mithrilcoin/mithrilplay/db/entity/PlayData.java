package io.mithrilcoin.mithrilplay.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Gamedata info DB
 */

@Entity(tableName = "playdata", indices = {@Index(value = {"packagename", "starttime", "endtime"}, unique = true)})
public class PlayData {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Integer idx;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "packagename")
    public String packagename;

    @ColumnInfo(name = "starttime")
    public String starttime;

    @ColumnInfo(name = "endtime")
    public String endtime;

    @ColumnInfo(name = "playTime")
    public String playTime;

    @ColumnInfo(name = "version")
    public String version;

    public PlayData(String email, String packagename, String starttime, String endtime, String playTime, String version) {
        this.email = email;
        this.packagename = packagename;
        this.starttime = starttime;
        this.endtime = endtime;
        this.playTime = playTime;
        this.version = version;
    }

}
