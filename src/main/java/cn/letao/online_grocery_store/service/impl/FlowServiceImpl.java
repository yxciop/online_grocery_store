package cn.letao.online_grocery_store.service.impl;

import cn.letao.online_grocery_store.dao.FlowMapper;
import cn.letao.online_grocery_store.pojo.Flow;
import cn.letao.online_grocery_store.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowServiceImpl implements FlowService {
    @Autowired
    private FlowMapper flowMapper;
    @Override
    public Integer deleteByPrimaryKey(String out_trade_no) {
        return flowMapper.deleteByPrimaryKey(out_trade_no);
    }

    @Override
    public Integer insert(Flow flow) {
        return flowMapper.insert(flow);
    }

    @Override
    public Flow selectByPrimaryKey(String out_trade_no) {
        return flowMapper.selectByPrimaryKey(out_trade_no);
    }

    @Override
    public Integer updateByPrimaryKey(Flow flow) {
        return flowMapper.updateByPrimaryKey(flow);
    }
}
