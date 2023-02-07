package cn.letao.online_grocery_store.dao;

import cn.letao.online_grocery_store.pojo.Cart;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CartMapper {
    Integer addCart(Cart cart);
    List<Cart> selectCartList(Integer buyerId);
    Integer updateSpecification(@Param(value="specification")String specification, @Param(value="id")Integer id);
    Integer updateAmount(@Param(value="amount")Integer amount, @Param(value="id")Integer id);
    Integer delCart(Integer id);
    Integer delByProduct_id(Integer product_id);
    List<Cart> ByArrayQueryCartList(Integer[] ids);
    Cart selectLikeCart(@Param(value="pid")Integer pid,
                        @Param(value="buyerId")Integer buyerId,
                        @Param(value="spe")String spe);
}
