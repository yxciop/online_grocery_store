package cn.letao.online_grocery_store.service.impl;

import cn.letao.online_grocery_store.dao.CartMapper;
import cn.letao.online_grocery_store.pojo.Cart;
import cn.letao.online_grocery_store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartServiceImpl  implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Override
    public Integer addCart(Cart cart) {
        return cartMapper.addCart(cart);
    }

    @Override
    public List<Cart> selectCartList(Integer buyerId) {
        return cartMapper.selectCartList(buyerId);
    }

    @Override
    public Integer updateAmount(Integer amount, Integer id) {
        return cartMapper.updateAmount(amount,id);
    }

    @Override
    public Integer updateSpecification(String specification, Integer id) {
        return cartMapper.updateSpecification(specification,id);
    }

    @Override
    public Integer delCart(Integer id) {
        return cartMapper.delCart(id);
    }

    @Override
    public Integer delByProduct_id(Integer product_id) {
        return cartMapper.delByProduct_id(product_id);
    }

    @Override
    public List<Cart> ByArrayQueryCartList(Integer[] ids) {
        return cartMapper.ByArrayQueryCartList(ids);
    }

    @Override
    public Cart selectLikeCart(Integer pid, Integer buyerId,String specification) {
        return cartMapper.selectLikeCart(pid,buyerId,specification);
    }
}
