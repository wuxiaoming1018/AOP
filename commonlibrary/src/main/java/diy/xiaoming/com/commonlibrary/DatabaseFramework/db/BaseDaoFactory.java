package diy.xiaoming.com.commonlibrary.DatabaseFramework.db;

import android.database.sqlite.SQLiteDatabase;

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
        sql ="data/data/diy.xiaoming.com.aop/jett.db";
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
