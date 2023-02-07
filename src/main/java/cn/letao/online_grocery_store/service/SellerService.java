package cn.letao.online_grocery_store.service;

import cn.letao.online_grocery_store.pojo.Seller;
import java.util.List;

public interface SellerService {
	Integer register(Seller seller);
	Seller querySeller(Seller seller);
	Integer updateSeller(Seller seller);
	Integer delSeller(Seller seller);
	List<Seller> queryAbnormalSeller(Integer pageNum, Integer pageSize);
	String  selectSellerMsg(Integer id);
}
