package cn.letao.online_grocery_store.service;

import cn.letao.online_grocery_store.pojo.Flow;

public interface FlowService {
    Integer deleteByPrimaryKey(String out_trade_no);

    Integer insert(Flow flow);

    Flow selectByPrimaryKey(String out_trade_no);

    Integer updateByPrimaryKey(Flow flow);
}
