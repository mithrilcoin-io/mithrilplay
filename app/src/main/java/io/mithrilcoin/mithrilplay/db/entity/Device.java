package io.mithrilcoin.mithrilplay.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Device info DB
 */

@Entity(tableName = "Device", indices = {@Index(value = "idx", unique = true)})
public class Device {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idx")
    @NonNull
    public Integer idx;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "model")
    public String model;

    @ColumnInfo(name = "brand")
    public String brand;

    @ColumnInfo(name = "useyn")
    public String useyn;

    @ColumnInfo(name = "osversion")
    public String osversion;

    public Device(String email, String model, String brand, String useyn, String osversion) {
        this.email = email;
        this.model = model;
        this.brand = brand;
        this.useyn = useyn;
        this.osversion = osversion;
    }

}
