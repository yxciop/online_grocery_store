package cn.letao.online_grocery_store.service.impl;

import cn.letao.online_grocery_store.dao.SellerMapper;
import cn.letao.online_grocery_store.pojo.Seller;
import cn.letao.online_grocery_store.service.SellerService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SellerServiceImpl  implements SellerService {
	@Autowired
	private SellerMapper sellerMapper;
	@Override
	public Integer register(Seller seller) {
		return sellerMapper.register(seller);
	}

	@Override
	public Seller querySeller(Seller seller) {
		return sellerMapper.querySeller(seller);
	}

	@Override
	public Integer updateSeller(Seller seller) {
		return sellerMapper.updateSeller(seller);
	}

	@Override
	public Integer delSeller(Seller seller) {
		return sellerMapper.delSeller(seller);
	}

	@Override
	public List<Seller> queryAbnormalSeller(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		return sellerMapper.queryAbnormalSeller();
	}

	@Override
	public String selectSellerMsg(Integer id) {
		return sellerMapper.selectSellerMsg(id);
	}
}
