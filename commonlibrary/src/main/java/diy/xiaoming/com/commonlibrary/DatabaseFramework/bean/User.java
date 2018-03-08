package diy.xiaoming.com.commonlibrary.DatabaseFramework.bean;

import diy.xiaoming.com.commonlibrary.DatabaseFramework.annotation.DBField;
import diy.xiaoming.com.commonlibrary.DatabaseFramework.annotation.DBTable;

/**
 * Created by Administrator on 2018-03-08.
 */
@DBTable("tb_user")
public class User {
    @DBField("_id")
    private Integer id;
    private String name;
    private String password;

    public User(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
