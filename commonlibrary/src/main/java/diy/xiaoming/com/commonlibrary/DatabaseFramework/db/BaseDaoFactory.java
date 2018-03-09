package diy.xiaoming.com.commonlibrary.DatabaseFramework.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2018-03-09.
 */

public class BaseDaoFactory {
    private static final BaseDaoFactory ourInstance = new BaseDaoFactory();

    public static BaseDaoFactory getOurInstance(){
        return ourInstance;
    }

    private String sql;
    private SQLiteDatabase sqLiteDatabase;

    private BaseDaoFactory(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //SDCard存在
            File file = Environment.getExternalStorageDirectory();
        }
        sql ="data/data/diy.xiaoming.com.aop/jettsd.db";
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sql,null);
    }

    /**
     * 用来生产basedao对象
     */
    public <T>BaseDao<T> getBaseDao(Class<T> entityClass){
       BaseDao baseDao = null;
        try {
            baseDao = BaseDao.class.newInstance();
            baseDao.init(sqLiteDatabase,entityClass);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseDao;
    }
}
