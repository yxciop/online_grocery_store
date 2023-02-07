package cn.letao.online_grocery_store.dao;

import cn.letao.online_grocery_store.pojo.Seller;
import java.util.List;

public interface SellerMapper {

	Seller querySeller(Seller seller);
	Integer register(Seller seller);
	Integer updateSeller(Seller seller);
	Integer delSeller(Seller seller);
	List<Seller> queryAbnormalSeller();
	String  selectSellerMsg(Integer id);
}
