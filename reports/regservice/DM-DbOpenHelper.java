package cn.richinfo.dm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.richinfo.dm.util.LogUtils;

/* loaded from: classes.dex */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "cmcc_dm.db";
    private static final String TAG = "DbOpenHelper";
    private static volatile DbOpenHelper instance;

    private DbOpenHelper(Context context) {
        super(context, DB_NAME, (SQLiteDatabase.CursorFactory) null, 64);
    }

    public static DbOpenHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DbOpenHelper.class) {
                if (instance == null) {
                    DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
                    LogUtils.debug(TAG, "new DbOpenHelper");
                    instance = dbOpenHelper;
                }
            }
        }
        return instance;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.beginTransaction();
        try {
            DbInit.createBatteryChargeTable(sQLiteDatabase);
            DbInit.createBluetoothTable(sQLiteDatabase);
            DbInit.createNetworkTable(sQLiteDatabase);
            DbInit.createTimeTrafficTable(sQLiteDatabase);
            DbInit.createAppMapTable(sQLiteDatabase);
            DbInit.createWifiListTable(sQLiteDatabase);
            sQLiteDatabase.setTransactionSuccessful();
        } finally {
            sQLiteDatabase.endTransaction();
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.beginTransaction();
        try {
            DbInit.dropBatteryChargeTable(sQLiteDatabase);
            DbInit.createBatteryChargeTable(sQLiteDatabase);
            DbInit.dropBluetoothTable(sQLiteDatabase);
            DbInit.createBluetoothTable(sQLiteDatabase);
            DbInit.dropNetworkTable(sQLiteDatabase);
            DbInit.createNetworkTable(sQLiteDatabase);
            DbInit.dropTimeTrafficTable(sQLiteDatabase);
            DbInit.createTimeTrafficTable(sQLiteDatabase);
            DbInit.dropOldAppTable(sQLiteDatabase);
            DbInit.dropAppMapTable(sQLiteDatabase);
            DbInit.createAppMapTable(sQLiteDatabase);
            DbInit.dropWifiListTable(sQLiteDatabase);
            DbInit.createWifiListTable(sQLiteDatabase);
            sQLiteDatabase.setTransactionSuccessful();
        } finally {
            sQLiteDatabase.endTransaction();
        }
    }
}
