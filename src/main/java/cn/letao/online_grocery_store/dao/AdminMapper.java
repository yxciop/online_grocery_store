package cn.letao.online_grocery_store.dao;

import cn.letao.online_grocery_store.pojo.Admin;

public interface AdminMapper {
    Admin queryAdmin(Admin admin);
    Integer updateStatus(Admin admin);
}
