package cn.letao.online_grocery_store.service.impl;

import cn.letao.online_grocery_store.dao.RoleMapper;
import cn.letao.online_grocery_store.pojo.Role;
import cn.letao.online_grocery_store.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role queryRoleInfo(int roleId) {
        return roleMapper.queryRoleInfo(roleId);
    }
}
