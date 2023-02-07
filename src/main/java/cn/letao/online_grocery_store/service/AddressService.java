package cn.letao.online_grocery_store.service;

import cn.letao.online_grocery_store.pojo.Address;
import java.util.List;

public interface AddressService {
    List<Address> queryAddress(Integer buyerId,Integer isDefault);
    Integer addAddress(Address address);
    Integer updateAddress(Address address);
    Integer delAddress(Integer id);
    Address queryAddressByID(Integer id);
}
