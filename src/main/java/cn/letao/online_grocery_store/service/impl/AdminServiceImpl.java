package cn.letao.online_grocery_store.service.impl;

import cn.letao.online_grocery_store.dao.AdminMapper;
import cn.letao.online_grocery_store.pojo.Admin;
import cn.letao.online_grocery_store.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl  implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Override
    public Admin queryAdmin(Admin admin) {
        return adminMapper.queryAdmin(admin);
    }

    @Override
    public Integer updateStatus(Admin admin) {
        return adminMapper.updateStatus(admin);
    }
}
