package io.mithrilcoin.mithrilplay.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import io.mithrilcoin.mithrilplay.db.entity.UserData;

/**
 * UserData DB access DAO
 */

@Dao
public interface UserDataDao {

    @Query("SELECT * FROM Userdata")
    UserData getAll();

    @Query("SELECT count(1) FROM Userdata " +
            "WHERE email = :email " +
            "AND registdate = :registdate " +
            "AND authdate = :authdate " +
            "AND modifydate = :modifydate " +
            "AND gender = :gender " +
            "AND birthyear = :birthyear " +
            "AND birthmonth = :birthmonth " +
            "AND country = :country " +
            "AND city = :city")
    int checkUserdata(String email, String registdate, String authdate, String modifydate, String gender,
                      String birthyear, String birthmonth, String country, String city);

//    @Query("UPDATE Userdata SET useyn = 'y' WHERE email = :emailId AND model = :model AND brand = :brand AND osversion = :osver")
//    int updateUseyn(String emailId, String model, String brand, String osver);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(UserData userData);

    @Update
    void update(UserData userData);

    @Delete
    void delete(UserData userData);

}
