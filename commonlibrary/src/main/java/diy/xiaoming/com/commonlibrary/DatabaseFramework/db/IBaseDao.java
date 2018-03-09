package diy.xiaoming.com.commonlibrary.DatabaseFramework.db;

import java.util.List;

/**
 * Created by Administrator on 2018-03-08.
 */

public interface IBaseDao<T> {
    long insert(T entity);

    long update(T entity,T where);
    int delete(T where);
    List<T> query(T where);
    List<T> query(T where,String orderBy,Integer startIndex,Integer limit);
    List<T> query(String sql);
}
