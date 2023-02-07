package cn.letao.online_grocery_store.controller;

import cn.letao.online_grocery_store.pojo.*;
import cn.letao.online_grocery_store.service.AddressService;
import cn.letao.online_grocery_store.service.CartService;
import cn.letao.online_grocery_store.service.ProductService;
import cn.letao.online_grocery_store.service.SellerService;
import cn.letao.online_grocery_store.util.GetImgList;
import cn.letao.online_grocery_store.util.MD5Util;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {
	@Resource
	private ProductService productService;
	@Resource
	private AddressService addressService;
	@Resource
	private SellerService  sellerService;
	@Resource
	private CartService cartService;
	@Autowired
	private GetImgList getImgList;

	@RequestMapping("/list")
	public String toShopList(
			@RequestParam(value = "id",required = false)Integer id/*商品类型id*/,
			@RequestParam(value = "idety",required = false)Integer idety,/*一级二级三级分类标识符,用于区分商品类型是几级分类*/
			@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,/*页码*/
			@RequestParam(defaultValue = "8",value = "pageSize") Integer pageSize,/*每页显示的数量*/
			@RequestParam(value = "keyword",required = false,defaultValue = "")String keyword,/*商品关联词*/
			@RequestParam(value = "condition",required = false,defaultValue = "")String condition,/*商品排序条件*/
			@RequestParam(value = "sort",required = false,defaultValue = "")String sort,/*商品排序方式，降序还是升序*/
			Model model){
		Product product=new Product();
		//判断有无商品排序条件
		if(condition.length()==0){
			//设置排序条件为商品修改时间
			product.setCondition("modify");
			//设置排序方法为降序
			product.setSort("down");
		}else{
			product.setCondition(condition);
			product.setSort(sort);
		}
		model.addAttribute("condition",condition);
		model.addAttribute("sort",sort);
		//查询前五个商品列表信息
		List<Product> productList=productService.queryProducts(product,pageNum,5);
		//建立分页对象，让每一页显示的商品轮播图不同
		PageInfo<Product> pageInfo=new PageInfo<>(productList);
		model.addAttribute("pics",pageInfo);
		if(keyword.length()>0){
			//设置商品关键词
			product.setTitle(keyword);
			model.addAttribute("keyword",keyword);
		}else{
			model.addAttribute("keyword","");
		}
		//判断是否有商品分类标识符
		if(idety==null){
			model.addAttribute("idety","");
		}else if(idety==1){
			//设置一级分类
			product.setType_1(id);
		}else if(idety==2){
			//设置二级分类
			product.setType_2(id);
		}else if(idety==3){
			//设置三级分类
			product.setType_3(id);
		}
		//判断有无商品分类标识符
		if(idety!=null){
			model.addAttribute("idety",idety);
		}
		//判断商品类型id是否为空
		if(id!=null){
			model.addAttribute("id",id);
		}else{
			model.addAttribute("id","");
		}
		//查询商品列表信息
		productList=productService.queryProducts(product,pageNum,pageSize);
		//建立分页对象
		pageInfo=new PageInfo<>(productList);
		model.addAttribute("pageInfo",pageInfo);
		return "html/shop/list";
	}
	@RequestMapping("/storeProduct")
	public String toStoreProduct(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,
								 @RequestParam(defaultValue = "5",value = "pageSize") Integer pageSize,
								 Integer sellerId,Model model){
		List<Product> productList=productService.selectSellerProduct(sellerId,1,3);
		PageInfo<Product> pageInfo=new PageInfo<>(productList);
		model.addAttribute("pics",pageInfo);
		Seller seller=new Seller();
		seller.setId(sellerId);
		Seller seller1=sellerService.querySeller(seller);
		model.addAttribute("storename",seller1.getStore_name());
		model.addAttribute("phonenumber",seller1.getPhonenumber());
		model.addAttribute("address",seller1.getAddress());
		model.addAttribute("sellerId",sellerId);
		productList=productService.selectSellerProduct(sellerId,pageNum,pageSize);
		pageInfo=new PageInfo<>(productList);
		model.addAttribute("pageInfo",pageInfo);
		return "html/shop/storeProduct";
	}
	@RequestMapping("/showDetail")
	public String todetail(Integer id,Model model){
		Product product=new Product();
		product.setId(id);
		Product pro=productService.selectProductDetail(product);
		List<String> imglist= new ArrayList<>();
		imglist=getImgList.getImgStr(pro.getDescription());
		model.addAttribute("pro",pro);
		model.addAttribute("pictures",imglist);
		return "html/shop/detail";
	}
	@RequestMapping("/order")
	public String toOrder(@RequestParam(required = false)Integer buyerId,
						  @RequestParam(required = false)Integer amount,
						  @RequestParam(required = false)Integer[] idArray,
						  @RequestParam(required = false,defaultValue = "")String specification,
			              @RequestParam(required = false)Integer product_id,
						  @RequestParam(required = false,defaultValue = "")String status,
						  @RequestParam(required = false)Integer id,
						  @RequestParam(required = false,defaultValue = "")String total,
						  Model model){
		//根据买家id查询默认收货信息
		List<Address> addresses=addressService.queryAddress(buyerId,1);
		//如果默认收货信息存在
		if(addresses.size()>0){
			addresses.get(0).setRname(MD5Util.convertMD5(addresses.get(0).getRname()));
			model.addAttribute("default",addresses.get(0));
		}
		//判断有无商品id传参
		if(product_id==null){
		}else{
			Product product=new Product();
			product.setId(product_id);
			Order order=new Order();
			//如果商品规格未定义
			if(specification.equals("undefined")){
				specification="";
			}else{
				order.setSpecification(specification);
			}
			//如果订单状态为空
			if(status.equals("")){
			}else{
				order.setStatus(status);
				order.setId(id);
			}
			order.setAmount(amount);
			DecimalFormat df=new DecimalFormat("#.00");
			//将订单数量转为double型
			Double num=Double.valueOf(amount);
			//计算订单总价格
			Double priceTotal=num*productService.selectProductDetail(product).getPrice();
			model.addAttribute("order",order);
            model.addAttribute("total",df.format(priceTotal));
			model.addAttribute("productId",product_id);
			model.addAttribute("product",productService.selectProductDetail(product));
		}
        if(idArray==null){
		}else {
        	//根据购物车id查询购物车列表信息
			List<Cart> cartList=cartService.ByArrayQueryCartList(idArray);
			model.addAttribute("cartList",cartList);
			model.addAttribute("idArray",JSON.toJSONString(idArray));
			DecimalFormat df=new DecimalFormat("#.00");
			model.addAttribute("total",df.format(new BigDecimal(total)));
		}
		return "html/shop/order";
	}
}
