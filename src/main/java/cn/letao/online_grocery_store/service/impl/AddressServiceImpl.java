package cn.letao.online_grocery_store.service.impl;

import cn.letao.online_grocery_store.dao.AddressMapper;
import cn.letao.online_grocery_store.pojo.Address;
import cn.letao.online_grocery_store.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public List<Address> queryAddress(Integer buyerId, Integer isDefault) {
        return addressMapper.queryAddress(buyerId, isDefault);
    }

    @Override
    public Integer addAddress(Address address) {
        return addressMapper.addAddress(address);
    }

    @Override
    public Integer updateAddress(Address address) {
        return addressMapper.updateAddress(address);
    }

    @Override
    public Integer delAddress(Integer id) {
        return addressMapper.delAddress(id);
    }

    @Override
    public Address queryAddressByID(Integer id) {
        return addressMapper.queryAddressByID(id);
    }
}
