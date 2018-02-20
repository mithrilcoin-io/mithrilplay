package io.mithrilcoin.mithrilplay.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import io.mithrilcoin.mithrilplay.db.entity.Device;
import io.mithrilcoin.mithrilplay.db.entity.PlayData;
import io.mithrilcoin.mithrilplay.db.entity.UserData;

/**
 * Created by Administrator on 2018-02-12.
 */

@Database( entities = {PlayData.class, UserData.class, Device.class}, version = MithrilDatabase.VERSION)
public abstract class MithrilDatabase extends RoomDatabase {

    static final int VERSION = 1;

    public abstract UserDataDao userDataDao();

    public abstract DeviceDao deviceDao();

    public abstract PlayDataDao playDataDao();

}
