package cn.letao.online_grocery_store.service;

import cn.letao.online_grocery_store.pojo.Admin;


public interface AdminService {
    Admin queryAdmin(Admin admin);
    Integer updateStatus(Admin admin);
}
