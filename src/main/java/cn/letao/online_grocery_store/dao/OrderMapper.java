package cn.letao.online_grocery_store.dao;

import cn.letao.online_grocery_store.pojo.Order;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface OrderMapper {
    List<Order>  queryOrders(Order order);
    List<Order>  selectUndisposedOrder();
    Integer updateOrder(Order order);
    Integer addOrder(Order order);
    Integer insertOrders(List<Order> orders);
    Integer updateOrderId(@Param(value="newOrderId")String newOrderId, @Param(value="id")Integer id);
    Integer deleteOrder(Integer id);
    Order queryProductOrderByProduct_id(Integer product_id);
    Order queryOrderDetailById(Integer id);
}
