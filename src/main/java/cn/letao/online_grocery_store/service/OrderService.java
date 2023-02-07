package cn.letao.online_grocery_store.service;

import cn.letao.online_grocery_store.pojo.Order;
import java.util.List;

public interface OrderService {
    List<Order> queryBuyerOrders(Order order);
    List<Order> querySellerOrder(Order order,Integer pageNum, Integer pageSize);
    List<Order>  selectUndisposedOrder(Integer pageNum, Integer pageSize);
    Integer updateOrder(Order order);
    Integer addOrder(Order order);
    Integer insertOrders(List<Order> orders);
    Integer updateOrderId(String newOrderId,Integer id);
    Integer deleteOrder(Integer id);
    Order queryProductOrderByProduct_id(Integer product_id);
    Order queryOrderDetailById(Integer id);
}
