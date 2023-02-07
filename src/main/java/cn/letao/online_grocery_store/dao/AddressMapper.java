package cn.letao.online_grocery_store.dao;

import cn.letao.online_grocery_store.pojo.Address;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface AddressMapper {
    List<Address> queryAddress(@Param(value="buyerId")Integer buyerId,@Param(value="isDefault")Integer isDefault);
    Integer addAddress(Address address);
    Integer updateAddress(Address address);
    Integer delAddress(Integer id);
    Address queryAddressByID(Integer id);
}
