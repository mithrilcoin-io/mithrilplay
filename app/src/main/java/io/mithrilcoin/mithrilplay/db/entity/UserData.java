package io.mithrilcoin.mithrilplay.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * User info DB
 */

@Entity( tableName = "Userdata", indices={@Index(value = "idx", unique = true)})
public class UserData {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idx")
    @NonNull
    public Integer idx;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "registdate")
    public String registdate;

    @ColumnInfo(name = "authdate")
    public String authdate;

    @ColumnInfo(name = "modifydate")
    public String modifydate;

    @ColumnInfo(name = "gender")
    public String gender;

    @ColumnInfo(name = "birthyear")
    public String birthyear;

    @ColumnInfo(name = "birthmonth")
    public String birthmonth;

    @ColumnInfo(name = "country")
    public String country;

    @ColumnInfo(name = "city")
    public String city;

}
