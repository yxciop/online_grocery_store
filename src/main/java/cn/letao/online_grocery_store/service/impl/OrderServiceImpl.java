package cn.letao.online_grocery_store.service.impl;

import cn.letao.online_grocery_store.dao.OrderMapper;
import cn.letao.online_grocery_store.pojo.Order;
import cn.letao.online_grocery_store.service.OrderService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public List<Order> queryBuyerOrders(Order order) {
        return orderMapper.queryOrders(order);
    }

    @Override
    public Integer updateOrder(Order order) {
        return orderMapper.updateOrder(order);
    }

    @Override
    public Integer addOrder(Order order) {
        return orderMapper.addOrder(order);
    }

    @Override
    public Integer insertOrders(List<Order> orders) {
        return orderMapper.insertOrders(orders);
    }

    @Override
    public Integer updateOrderId(String newOrderId, Integer id) {
        return orderMapper.updateOrderId(newOrderId,id);
    }

    @Override
    public Integer deleteOrder(Integer id) {
        return orderMapper.deleteOrder(id);
    }

    @Override
    public Order queryProductOrderByProduct_id(Integer product_id) {
        return orderMapper.queryProductOrderByProduct_id(product_id);
    }

    @Override
    public List<Order> querySellerOrder(Order order,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return orderMapper.queryOrders(order);
    }

    @Override
    public List<Order> selectUndisposedOrder(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return orderMapper.selectUndisposedOrder();
    }

    @Override
    public Order queryOrderDetailById(Integer id) {
        return orderMapper.queryOrderDetailById(id);
    }
}
