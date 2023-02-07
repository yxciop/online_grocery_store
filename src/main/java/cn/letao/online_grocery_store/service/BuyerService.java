package cn.letao.online_grocery_store.service;

import cn.letao.online_grocery_store.pojo.Buyer;
import cn.letao.online_grocery_store.pojo.BuyerVo;
import java.util.List;

public interface BuyerService {
	/**
	 *
	 * @param buyer
	 * @return
	 */
	Integer register(Buyer buyer);
	Buyer queryBuyer(BuyerVo buyer);
	Integer updateBuyer(Buyer buyer);
	Integer delBuyer(Integer id);
	List<Buyer> queryAbnormalUser(Integer pageNum, Integer pageSize);
	String selectBuyerMsg(Integer id);
}
