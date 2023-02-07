package cn.letao.online_grocery_store.dao;

import cn.letao.online_grocery_store.pojo.Buyer;
import cn.letao.online_grocery_store.pojo.BuyerVo;
import java.util.List;

public interface BuyerMapper {
	Integer register(Buyer buyer);
	Buyer queryBuyer(BuyerVo buyer);
	Integer updateBuyer(Buyer buyer);
	Integer delBuyer(Integer id);
	List<Buyer> queryAbnormalUser();
	String selectBuyerMsg(Integer id);
}