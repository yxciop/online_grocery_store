package cn.letao.online_grocery_store.service.impl;

import cn.letao.online_grocery_store.dao.Product_TypeMapper;
import cn.letao.online_grocery_store.pojo.Product_Type;
import cn.letao.online_grocery_store.service.Product_TypeService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Product_TypeServiceImpl implements Product_TypeService {
	@Autowired
	private Product_TypeMapper product_typeMapper;
	@Override
	public List<Product_Type> getProductTypes(int parentId) {
		return product_typeMapper.getProductTypes(parentId);
	}

	@Override
	public Integer addProductType(Product_Type product_type) {
		return product_typeMapper.addProductType(product_type);
	}

	@Override
	public Integer updateProductTypeSoncode(Product_Type product_type) {
		return product_typeMapper.updateProductTypeSoncode(product_type);
	}

	@Override
	public List<Product_Type> getTypesDetail(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		return product_typeMapper.getTypesDetail();
	}

	@Override
	public Integer deleteType(Integer id) {
		return product_typeMapper.deleteType(id);
	}

	@Override
	public String queryTypeById(Integer id) {
		return product_typeMapper.queryTypeById(id);
	}
}
