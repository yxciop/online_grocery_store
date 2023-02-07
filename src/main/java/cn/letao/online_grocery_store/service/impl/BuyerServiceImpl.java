package cn.letao.online_grocery_store.service.impl;

import cn.letao.online_grocery_store.dao.BuyerMapper;
import cn.letao.online_grocery_store.pojo.Buyer;
import cn.letao.online_grocery_store.pojo.BuyerVo;
import cn.letao.online_grocery_store.service.BuyerService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BuyerServiceImpl implements BuyerService {
	@Autowired
	private BuyerMapper buyerMapper;
	@Override
	public Integer register(Buyer buyer) {
		return buyerMapper.register(buyer);
	}

	public Buyer queryBuyer(BuyerVo buyer) {
		return buyerMapper.queryBuyer(buyer);
	}

	@Override
	public Integer updateBuyer(Buyer buyer) {
		return buyerMapper.updateBuyer(buyer);
	}

	@Override
	public Integer delBuyer(Integer id) {
		return buyerMapper.delBuyer(id);
	}

	@Override
	public List<Buyer> queryAbnormalUser(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		return buyerMapper.queryAbnormalUser();
	}

	@Override
	public String selectBuyerMsg(Integer id) {
		return buyerMapper.selectBuyerMsg(id);
	}


}
