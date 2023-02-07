package cn.letao.online_grocery_store.service;

import cn.letao.online_grocery_store.pojo.Product;
import java.util.List;

public interface ProductService {
    Integer addProduct(Product product);
    List<Product> queryProducts(Product product,Integer pageNum, Integer pageSize);
    List<Product> successUnreviewedProducts(Integer cout);
    List<Product> selectUnreviewedProducts(Integer pageNum, Integer pageSize);
    Product selectProductDetail(Product product);
    Integer updateProduct(Product product);
    Integer delProduct(Integer id);
    Integer subtractStock(Integer amount,Integer id);
    List<Product> selectSellerProduct(Integer sellerId,Integer pageNum,Integer pageSize);

}
