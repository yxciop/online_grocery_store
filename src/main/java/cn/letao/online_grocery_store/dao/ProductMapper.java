package cn.letao.online_grocery_store.dao;

import cn.letao.online_grocery_store.pojo.Product;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ProductMapper {
    Integer addProduct(Product product);
    List<Product> queryProducts(Product product);
    List<Product> selectUnreviewedProducts(Integer cout);
    Product selectProductDetail(Product product);
    Integer updateProduct(Product product);
    Integer delProduct(Integer id);
    Integer subtractStock(@Param(value="amount")Integer amount, @Param(value="id")Integer id);
    List<Product> selectSellerProduct(Integer sellerId);
}
