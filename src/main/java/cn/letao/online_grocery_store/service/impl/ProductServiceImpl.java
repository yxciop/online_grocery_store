package cn.letao.online_grocery_store.service.impl;

import cn.letao.online_grocery_store.dao.ProductMapper;
import cn.letao.online_grocery_store.pojo.Product;
import cn.letao.online_grocery_store.service.ProductService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl  implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    public Integer addProduct(Product product) {
        return productMapper.addProduct(product);
    }

    @Override
    public List<Product> queryProducts(Product product,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return productMapper.queryProducts(product);
    }

    @Override
    public List<Product> successUnreviewedProducts(Integer cout) {
        return productMapper.selectUnreviewedProducts(cout);
    }

    @Override
    public List<Product> selectUnreviewedProducts(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return productMapper.selectUnreviewedProducts(null);
    }

    @Override
    public Product selectProductDetail(Product product) {
        return productMapper.selectProductDetail(product);
    }


    @Override
    public Integer updateProduct(Product product) {
        return productMapper.updateProduct(product);
    }

    @Override
    public Integer delProduct(Integer id) {
        return productMapper.delProduct(id);
    }

    @Override
    public Integer subtractStock(Integer amount, Integer id) {
        return productMapper.subtractStock(amount,id);
    }

    @Override
    public List<Product> selectSellerProduct(Integer sellerId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return productMapper.selectSellerProduct(sellerId);
    }


}
