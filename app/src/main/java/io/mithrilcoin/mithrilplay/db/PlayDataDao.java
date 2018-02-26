package io.mithrilcoin.mithrilplay.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.mithrilcoin.mithrilplay.db.entity.PlayData;
import io.mithrilcoin.mithrilplay.network.vo.GamePlayDataRequest;

/**
 * GameData DB access DAO
 */

@Dao
public interface PlayDataDao {

    @Query("SELECT * FROM playdata")
    List<PlayData> getAll();

    @Query("SELECT * FROM playdata WHERE packageName LIKE :packageName")
    PlayData findByPackage(String packageName);

    @Query("SELECT max(endtime) FROM playdata")
    String getLastEndtime();

    @Query("SELECT packagename, starttime, endtime, version FROM playdata WHERE endtime >= :time and email = :email ORDER BY packageName desc")
    List<GamePlayDataRequest> getTodayGameList(long time, String email);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<PlayData> data);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PlayData data);

    @Update
    void update(PlayData data);

    @Delete
    void delete(PlayData data);

}
