package cn.letao.online_grocery_store.service;

import cn.letao.online_grocery_store.pojo.Cart;
import java.util.List;

public interface CartService {
    Integer addCart(Cart cart);
    List<Cart> selectCartList(Integer buyerId);
    Integer updateAmount(Integer amount, Integer id);
    Integer updateSpecification(String specification,Integer id);
    Integer delCart(Integer id);
    Integer delByProduct_id(Integer product_id);
    List<Cart> ByArrayQueryCartList(Integer[] ids);
    Cart selectLikeCart(Integer pid,Integer buyerId,String specification);
}
