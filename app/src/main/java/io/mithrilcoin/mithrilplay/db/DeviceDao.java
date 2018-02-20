package io.mithrilcoin.mithrilplay.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.mithrilcoin.mithrilplay.db.entity.Device;

/**
 *  DeviceData DB access DAO
 */

@Dao
public interface DeviceDao {

    @Query("SELECT * FROM Device")
    List<Device> getAll();

    @Query("SELECT count(1) FROM Device WHERE osversion = :osver AND email = :emailId")
    int checkEmailOsver(String osver, String emailId);

    @Query("UPDATE Device SET useyn = 'y' WHERE email = :emailId AND model = :model AND brand = :brand AND osversion = :osver")
    int updateUseyn(String emailId, String model, String brand, String osver);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Device device);

    @Update
    void update(Device device);

    @Delete
    void delete(Device device);

}
