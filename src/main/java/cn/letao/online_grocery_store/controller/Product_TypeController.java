package cn.letao.online_grocery_store.controller;

import cn.letao.online_grocery_store.pojo.Product_Type;
import cn.letao.online_grocery_store.service.Product_TypeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/type")
public class Product_TypeController {
	@Resource
	private Product_TypeService product_typeService;

	@CrossOrigin
	@ResponseBody
	@RequestMapping("/query/{parentId}")
	public Product_Type[] queryProductType(@PathVariable int parentId){
		List<Product_Type> typeList= product_typeService.getProductTypes(parentId);//根据父类id获取商品分类
		Product_Type data[]=new Product_Type[typeList.size()];//定义商品分类数组
		//for循环把商品分类数据添加到数据
		for(int i=0;i<typeList.size();i++){
			data[i]=typeList.get(i);
		}
		return data;
	}
}
