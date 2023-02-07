package cn.letao.online_grocery_store.dao;

import cn.letao.online_grocery_store.pojo.Product_Type;
import java.util.List;

public interface Product_TypeMapper {
	List<Product_Type> getProductTypes(int parentId);
	Integer addProductType(Product_Type product_type);
	Integer updateProductTypeSoncode(Product_Type product_type);
	List<Product_Type> getTypesDetail();
	Integer deleteType(Integer id);
	String queryTypeById(Integer id);
}
