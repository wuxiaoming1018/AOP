package diy.xiaoming.com.commonlibrary.DatabaseFramework.db;

/**
 * Created by Administrator on 2018-03-08.
 */

public class BaseDao<T> implements IBaseDao<T> {
    @Override
    public long insert(T entity) {
        return 0;
    }
}
