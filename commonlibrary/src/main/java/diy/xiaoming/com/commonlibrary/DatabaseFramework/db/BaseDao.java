package diy.xiaoming.com.commonlibrary.DatabaseFramework.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import diy.xiaoming.com.commonlibrary.DatabaseFramework.annotation.DBField;
import diy.xiaoming.com.commonlibrary.DatabaseFramework.annotation.DBTable;

/**
 * Created by Administrator on 2018-03-08.
 */

public class BaseDao<T> implements IBaseDao<T> {

    private SQLiteDatabase sqLiteDatabase;//持有数据库操作的引用
    private String tableName;//表名
    private Class<T> entityClass;//持有操作数据库所对应的java类型
    private boolean isInit = false;//是否做过初始化操作
    private HashMap<String, Field> cacheMap;//缓存空间（key--字段名  value--成员变量）

    @Override
    public long insert(T entity) {
        Map<String,String> map = getValues(entity);
        ContentValues values = getContentValues(map);
        long result = sqLiteDatabase.insert(tableName,null,values);
        return 0;
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set<String> keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key,value);
            }
        }
        return contentValues;
    }

    private Map<String, String> getValues(T entity) {
        Map<String,String> map = new HashMap<>();
        Iterator<Field> iterator = cacheMap.values().iterator();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            field.setAccessible(true);
            //获取成员变量的值
            try {
                Object object = field.get(entity);
                if (object == null) {
                    continue;
                }
                String value = object.toString();
                //获取列名
                String key = null;
                if (field.getAnnotation(DBField.class).value()!=null) {
                    key = field.getAnnotation(DBField.class).value();
                }else{
                    key = field.getName();
                }
                if (!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)) {
                    map.put(key,value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    protected boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entityClass) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entityClass = entityClass;
        if (!isInit) {
            if (entityClass.getAnnotation(DBTable.class) == null) {
                tableName = entityClass.getSimpleName();
            } else {
                tableName = entityClass.getAnnotation(DBTable.class).value();
            }
            if (!sqLiteDatabase.isOpen()) {
                return false;
            }
            String createTableSql = getCreateTableSql();
            sqLiteDatabase.execSQL(createTableSql);
            cacheMap = new HashMap<>();
            initCacheMap();
            isInit = true;
        }
        return isInit;
    }

    private void initCacheMap() {
        //1、获取所有列名
        String sql = "select * from "+tableName+" limit 1,0";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        cursor.close();
        //2、获取所有成员变量
        Field[] columnFields = entityClass.getDeclaredFields();
        //打开所有字段的访问权限
        for (Field field : columnFields) {
            field.setAccessible(true);
        }
        //对1和2进行映射
        for (String columnName : columnNames) {
            Field columnField = null;
            for (Field field : columnFields) {
                String fieldName = null;
                if(field.getAnnotation(DBField.class)!=null){
                    fieldName = field.getAnnotation(DBField.class).value();
                }else{
                    fieldName = field.getName();
                }
                if (columnName.equals(fieldName)) {
                    columnField = field;
                }
            }
            if (columnField != null) {
                cacheMap.put(columnName,columnField);
            }
        }
    }

    private String getCreateTableSql() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists");
        stringBuffer.append(tableName + "(");
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            Class type = field.getType();//拿到成员类型
            if (field.getAnnotation(DBField.class) != null) {
                if (type == String.class) {
                    stringBuffer.append(field.getAnnotation(DBField.class).value() + " TEXT,");
                } else if (type == Integer.class) {
                    stringBuffer.append(field.getAnnotation(DBField.class).value() + " INTEGER,");
                } else if (type == Long.class) {
                    stringBuffer.append(field.getAnnotation(DBField.class).value() + " BIGINT,");
                } else if (type == Double.class) {
                    stringBuffer.append(field.getAnnotation(DBField.class).value() + " DOUBLE,");
                } else if (type == byte[].class) {
                    stringBuffer.append(field.getAnnotation(DBField.class).value() + " BLOB,");
                } else {
                    //不支持的类型
                    continue;
                }
            } else {
                if (type == String.class) {
                    stringBuffer.append(field.getName() + " TEXT,");
                } else if (type == Integer.class) {
                    stringBuffer.append(field.getName() + " INTEGER,");
                } else if (type == Long.class) {
                    stringBuffer.append(field.getName() + " BIGINT,");
                } else if (type == Double.class) {
                    stringBuffer.append(field.getName() + " DOUBLE,");
                } else if (type == byte[].class) {
                    stringBuffer.append(field.getName() + " BLOB,");
                } else {
                    //不支持类型
                    continue;
                }
            }
        }
        if (stringBuffer.charAt(stringBuffer.length() - 1) == ',') {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }
}
