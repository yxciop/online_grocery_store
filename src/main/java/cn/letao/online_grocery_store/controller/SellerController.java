package cn.letao.online_grocery_store.controller;

import cn.letao.online_grocery_store.pojo.*;
import cn.letao.online_grocery_store.service.*;
import cn.letao.online_grocery_store.util.GetImgList;
import cn.letao.online_grocery_store.util.MD5Util;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/seller")
public class SellerController {
	@Resource
	private SellerService sellerService;
	@Resource
	private Product_TypeService product_typeService;
	@Resource
	private ProductService productService;
	@Resource
	private OrderService orderService;
	@Resource
	private BuyerService buyerService;
	@Resource
	private FlowService flowService;
	@Resource
	private CartService cartService;
	@Autowired
	private GetImgList getImgList;

	@RequestMapping("/tologin")
	public String toLogin(){
		return "html/seller/LoginAndRegister";
	}
	@PostMapping("/login")
	public String login(Seller seller,Model model, HttpSession session){
		if(session.getAttribute("seller")==null){
			Seller seller1=sellerService.querySeller(new Seller(seller.getUsername(),null,null));
			if(seller1==null){
				model.addAttribute("login_username_msg","用户名不存在");
				return "html/seller/LoginAndRegister";
			}else{
				if(!seller1.getPassword().equals(MD5Util.stringMD5(seller.getPassword()))){
					model.addAttribute("login_password_msg","密码错误");
					return "html/seller/LoginAndRegister";
				}else{
					if(seller1.getLoginStatus().equals("login")){
						model.addAttribute("login_username_msg","用户已在别处登陆过,请稍后登录");
						return "html/seller/LoginAndRegister";
					}else{
						seller1.setLoginStatus("login");
						sellerService.updateSeller(seller1);
						seller1.setPassword(seller.getPassword());
						String realname=MD5Util.convertMD5(seller1.getRealname());
						seller1.setRealname(realname);
						session.setAttribute("seller",seller1);
						model.addAttribute("login_msg","登录成功");
					}
				}
			}
		}
		return "html/seller/success";
	}
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		Seller seller= (Seller) session.getAttribute("seller");
		seller.setPassword(MD5Util.stringMD5(seller.getPassword()));
		seller.setRealname(MD5Util.convertMD5(seller.getRealname()));
		seller.setLoginStatus("logout");
		sellerService.updateSeller(seller);
		session.removeAttribute("seller");
		return "html/seller/LoginAndRegister";
	}
	@RequestMapping("/tofind")
	public String tofindPass(){
		return "html/seller/findPassword";
	}

	@ResponseBody
	@PostMapping("/find")
	public HashMap<String,String> changePass(Seller seller){
		HashMap<String,String> result=new HashMap<>();
		//根据用户名查询有无相关卖家
		Seller seller1=sellerService.querySeller(new Seller(seller.getUsername(),null,null));
		//如果未查询到该用户名
		if(seller1==null){
			result.put("userFail","用户名不存在,无法修改密码");
		}else{
			//如果输入的手机号码与真实的不匹配
			if(!seller.getPhonenumber().equals(seller1.getPhonenumber())){
				result.put("phoneFail","手机号码信息错误");
			}
			//如果输入的真实姓名与真实的不匹配
			if(!seller.getRealname().equals(MD5Util.convertMD5(seller1.getRealname()))){
				result.put("nameFail","姓名信息错误");
			}
			if (seller.getPhonenumber().equals(seller1.getPhonenumber()) &&
					seller.getRealname().equals(MD5Util.convertMD5(seller1.getRealname()))){
				seller.setId(seller1.getId());
				seller.setPassword(MD5Util.stringMD5(seller.getPassword()));
				//更改卖家信息
				sellerService.updateSeller(seller);
				result.put("change_msg","密码修改成功");
			}
		}
		return result;
	}
	@PostMapping("/register")
	public String register(Seller seller, Model model, @RequestParam(value = "pic_path",required = false) MultipartFile pic, HttpServletRequest request){
		String headPicPath = "";
		String headLocPath = "";
		String path="";
		path = request.getSession().getServletContext().getRealPath("static/uploadfiles" + File.separator + "sellerHead");//获取上传文件的路径
		String oldFileName = pic.getOriginalFilename();
		String prefix = FilenameUtils.getExtension(oldFileName);
		Seller usernameRepeat=sellerService.querySeller(new Seller(seller.getUsername(),null,null));
		Seller phonenumberRepeat=sellerService.querySeller(new Seller(null,seller.getPhonenumber(),null));
		Seller storenameRepeat=sellerService.querySeller(new Seller(null,null,seller.getStore_name()));
		if(usernameRepeat!=null){
			model.addAttribute("username_msg","用户名已存在");
		}
		if(phonenumberRepeat!=null){
			model.addAttribute("phone_msg","手机号码已注册过其他账号");
		}
		if(storenameRepeat!=null){
			model.addAttribute("storeName_msg","已有商家使用此名称");
		}
		if(usernameRepeat==null&&phonenumberRepeat==null&&storenameRepeat==null){
			if(!pic.isEmpty()){
				//设置文件名
				String fileName = seller.getUsername() + "卖家头像."+prefix;
				//创建目标文件对象用来上传文件
				File targetFile = new File(path, fileName);
				//判断文件夹是否存在
				if (!targetFile.exists()) {
					//创建子目录
					targetFile.mkdir();
				}
				try {
					pic.transferTo(targetFile);//上传文件
				}catch (IOException e){
					e.printStackTrace();
				}
				headPicPath = request.getContextPath() + "/static/uploadfiles/sellerHead/" + fileName;
				headLocPath = path + File.separator + fileName;
				seller.setImgPath(headPicPath);
				seller.setImgPhysicalPath(headLocPath);
				seller.setPassword(MD5Util.stringMD5(seller.getPassword()));
				seller.setRealname(MD5Util.convertMD5(seller.getRealname()));
				//添加卖家信息
				sellerService.register(seller);
				model.addAttribute("reg_msg", "注册成功");
			}
		}
		return "html/seller/LoginAndRegister";
	}
     @RequestMapping("/toaddProduct")
	public String toadd(Model model){
		List<Product_Type> product_types=product_typeService.getProductTypes(0);
		model.addAttribute("oneTypes",product_types);
		return "html/seller/addProduct";
	}
	@RequestMapping("/showProduct")
	public String toSellerProductList(Model model,HttpSession session,
									  @RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum,
									  @RequestParam(defaultValue = "2",value = "pageSize") Integer pageSize){
		Seller seller= (Seller) session.getAttribute("seller");
		List<Product> list=productService.selectSellerProduct(seller.getId(),pageNum,pageSize);
		PageInfo<Product> pageInfo=new PageInfo<>(list);
		model.addAttribute("pageInfo",pageInfo);
		return "html/seller/productlist";
	}
	@RequestMapping("/toUpdatePassword")
	public String toUpdatePasswordPage(){
		return "html/seller/updatePassword";
	}
	@RequestMapping("/updatePassword")
	public String updatePassword(String password,HttpSession session,Model model){
		//获取保存的登录信息
		Seller seller= (Seller) session.getAttribute("seller");
		Seller seller1=seller;
		seller.setPassword(MD5Util.stringMD5(password));
		//更改密码
		sellerService.updateSeller(seller);
		//更改保存信息
		seller1.setPassword(password);
		session.setAttribute("seller",seller1);
		model.addAttribute("msg","修改成功");

		return "html/seller/updatePassword";
	}
	@RequestMapping("/toUpdateInfo")
	public String toupdateInfo(){
		 return "html/seller/updateInfo";
	}
	@ResponseBody
	@PostMapping ("/updateInfomation")
	public HashMap<String,String>  updateInfo(
			Seller seller, @RequestParam(value = "mainPic",required = false) MultipartFile mainPic,
			HttpServletRequest request,HttpSession session,Integer id){
		HashMap<String,String> result=new HashMap<>();
		String path="";
		String headPicPath = "";
		String headLocPath = "";
		path = request.getSession().getServletContext().getRealPath("static/uploadfiles" + File.separator + "sellerHead");//获取上传文件的路径
		String oldFileName = mainPic.getOriginalFilename();
		String prefix = FilenameUtils.getExtension(oldFileName);
		//根据手机号获取卖家信息
		Seller phonenumberRepeat=sellerService.querySeller(new Seller(null,seller.getPhonenumber(),null));
		//根据店铺名称获取卖家信息
		Seller storenameRepeat=sellerService.querySeller(new Seller(null,null,seller.getStore_name()));
		//获取session保存的卖家信息
		Seller seller1=(Seller)session.getAttribute("seller");
		int status=1;
		//如果有相同手机号的用户
		if(phonenumberRepeat!=null){
			//如果手机号与保存的登录信息的手机号不同
			if(!seller1.getPhonenumber().equals(seller.getPhonenumber())){
				status=0;
				result.put("phone_msg","手机号码已注册过其他账号");
			}else{
				phonenumberRepeat=null;
			}
		}
		//如果有相同店铺名称的用户
		if(storenameRepeat!=null){
			//如果店铺名称与保存的登录信息的店铺名称不同
			if(!seller1.getStore_name().equals(seller.getStore_name())){
				status=0;
				result.put("storeName_msg","已有商家使用此名称");
			}else {
				storenameRepeat=null;
			}
		}
		//如果都没有相同的手机号或店铺名称的用户
		if(phonenumberRepeat==null&&storenameRepeat==null){
			if(!mainPic.isEmpty()){
				String mainPicPath = seller1.getImgPhysicalPath();
				File file = new File(mainPicPath);
				//判断头像文件是否存在
				if (file.exists()) {
					file.delete();
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				//设置头像文件名
				String fileName = seller.getId()+seller.getUsername()+df.format(new Date())+ "卖家头像."+prefix;
				fileName=fileName.replace("*","");
				fileName=fileName.replace("\\","");
				fileName=fileName.replace("/","");
				fileName=fileName.replace(":","");
				fileName=fileName.replace("?","");
				fileName=fileName.replace("\"","");
				fileName=fileName.replace("<","");
				fileName=fileName.replace(">","");
				fileName=fileName.replace("|","");
				//创建目标文件对象用来上传文件
				File targetFile = new File(path, fileName);
				//判断文件夹是否存在
				if (!targetFile.exists()) {
					//创建子目录
					targetFile.mkdir();
				}
				try {
					mainPic.transferTo(targetFile);//上传文件
				}catch (IOException e){
					e.printStackTrace();
				}
				headPicPath = request.getContextPath() + "/static/uploadfiles/sellerHead/" + fileName;
				headLocPath = path + File.separator + fileName;
			}
		}
		if(status==1){
			seller.setId(seller1.getId());
			seller.setImgPath(headPicPath);
			seller.setImgPhysicalPath(headLocPath);
			sellerService.updateSeller(seller);
			result.put("msg", "修改成功");
			seller1.setPhonenumber(seller.getPhonenumber());
			seller1.setAddress(seller.getAddress());
			seller1.setStore_name(seller.getStore_name());
			if(!headPicPath.equals("")){
				seller1.setImgPath(headPicPath);
				seller1.setImgPhysicalPath(headLocPath);
			}
			session.setAttribute("seller",seller1);
		}
		return result;
	}
	@RequestMapping("/showStatus")
	public String showStatus(){
		return "html/seller/queryStatus";
	}
	@ResponseBody
	@PostMapping("/addmsg")
	public HashMap<String,String> addmsg(HttpSession session,String msg){
		HashMap<String,String> result=new HashMap<>();
		//获取保存的卖家信息
		Seller seller=(Seller) session.getAttribute("seller");
		//设置异常信息
		seller.setMsg(msg);
		//更改卖家信息
		sellerService.updateSeller(seller);
		//更改保存的信息
		session.setAttribute("seller",seller);
		result.put("data","success");
		return result;
	}
	@RequestMapping("/deleteSeller")
	public String deleteSELLER(HttpSession session,HttpServletRequest request) throws NullPointerException{
		Seller seller=(Seller) session.getAttribute("seller");
		String mainPicPath = seller.getImgPhysicalPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
		String physicalPath=request.getSession().getServletContext().getRealPath("/");
		File file = new File(mainPicPath);
		if (file.exists()) {
			file.delete();
		}
		if(seller.getMsg()== null || seller.getMsg().equals("")) {
		}else{
			List imglist = getImgList.getImgStr(seller.getMsg());
			if (imglist.size() > 0) {
				for (int i = 0; i < imglist.size(); i++) {
					String str = (String) imglist.get(i);
					str = str.replace(basePath, "");
					str = str.replace("/", "\\\\");
					File file1 = new File(physicalPath + str);
					if (file1.exists()) {
						file1.delete();
					}
				}
			}
		}
		sellerService.delSeller(seller);
		session.removeAttribute("seller");
		return "html/seller/LoginAndRegister";
	}
	@RequestMapping("/orderAdmin")
	public String showOrderAdmin(){
		return "html/seller/orderDispose";
	}
	@RequestMapping("/deleteOrder")
	public String showDeleteOrderList(){
		return "html/seller/delOrder";
	}
	@ResponseBody
	@PostMapping("/complainOrder")
	public String complain(Integer id,String oid,Integer buyerId,String complain){
		JSONObject json=new JSONObject();
		Order order=new Order();
		order.setId(id);
		order.setStatus("退款失败");
		order.setMsg("卖家:买家恶意操作退款-------"+"投诉原因:"+complain);
		//根据id更改订单状态和信息
		int i=orderService.updateOrder(order);
		Buyer buyer=new Buyer();
		buyer.setId(buyerId);
		buyer.setStatus("异常");
		buyer.setMsg("订单"+oid+"的卖家投诉该账户");
		//根据id更改买家的状态和异常信息
		int k=buyerService.updateBuyer(buyer);
		if(i==1&&k==1){
			json.put("msg","投诉成功");
		}else{
			json.put("msg","投诉失败");
		}
		return JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/denyRefund")
	public String denyRefund(Integer id){
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		Order order=new Order();
		order.setId(id);
		order.setStatus("退款失败");
		order.setMsg("卖家拒绝你的退款");
		//根据id更改订单状态和信息
		int i=orderService.updateOrder(order);
		if(i==1){
			json.put("msg","拒绝成功");
		}else{
			json.put("msg","拒绝失败");
		}
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/acceptOrder")
	public String accept(Integer id){
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		Order order=new Order();
		order.setId(id);
		order.setStatus("已接受订单");
		order.setMsg("卖家已收到订单信息");
		//根据id更改订单状态和信息
		int i=orderService.updateOrder(order);
		if(i==1){
			json.put("msg","接受成功");
		}else{
			json.put("msg","接受失败");
		}
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/bill")
	public String bill(Integer id){
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		Order order=new Order();
		order.setId(id);
		order.setStatus("已发货");
		order.setMsg("卖家已发货，等待派送员收件");
		//根据id更改订单状态和信息
		int i=orderService.updateOrder(order);
		if(i==1){
			json.put("msg","发货成功");
		}else{
			json.put("msg","发货失败");
		}
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/delivery")
	public String delivery(Integer id){
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		Order order=new Order();
		order.setId(id);
		order.setStatus("送货中");
		order.setMsg("派送员已收件，派送员正在火速派送商品中");
		//根据id更改订单状态和信息
		int i=orderService.updateOrder(order);
		if(i==1){
			json.put("msg","派送成功");
		}else{
			json.put("msg","派送失败");
		}
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/reach")
	public String reach(Integer id){
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		Order order=new Order();
		order.setId(id);
		order.setStatus("已送达");
		order.setMsg("商品已到达目的地");
		//根据id更改订单状态和信息
		int i=orderService.updateOrder(order);
		if(i==1){
			json.put("msg","已送达");
		}else{
			json.put("msg","送达失败");
		}
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/showSellerOrderList")
	public String getSellerOrder(
			Integer sellerId,
			@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,
			@RequestParam(defaultValue = "5",value = "pageSize") Integer pageSize){
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		Order order=new Order();
		order.setSellerId(sellerId);
		//根据卖家id分页查询相关订单信息
		List<Order> orderList=orderService.querySellerOrder(order,pageNum,pageSize);
		PageInfo<Order> pageInfo=new PageInfo<>(orderList);
		json.put("pageInfo",pageInfo);
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/delOrderList")
	public String showDeleteOrderList(HttpSession session,
									  @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,
									  @RequestParam(defaultValue = "10",value = "pageSize") Integer pageSize){
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		Seller seller= (Seller) session.getAttribute("seller");
		Integer sellerId=seller.getId();
		Order order=new Order();
		order.setSellerId(sellerId);
		order.setBuyerId(0);
		List<Order> orderList=orderService.querySellerOrder(order,pageNum,pageSize);
		PageInfo<Order> pageInfo=new PageInfo<>(orderList);
		json.put("pageInfo",pageInfo);
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/deleteSellerOrder")
	public String deleteSellerOrder(Integer id){
		int k=1;
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		//根据id获取订单信息
		Order order=orderService.queryOrderDetailById(id);
		//获取订单号
		String out_trade_no=order.getOrderId();
		order.setId(null);
		order.setOrderId(out_trade_no);
		//查询相关订单号的订单个数
		Integer count=orderService.queryBuyerOrders(order).size();
		//根据订单号查询流水信息
		Flow flow=flowService.selectByPrimaryKey(out_trade_no);
		//如果没有流水信息
		if(flow==null){
		}else{
			//如果订单个数只有一个
			if(count==1){
				//根据订单号删除流水信息
				k=flowService.deleteByPrimaryKey(out_trade_no);
			}
		}
		//根据id删除订单
		int i=orderService.deleteOrder(id);
		if(i==1&&k==1){
			json.put("msg","删除成功");
		}else{
			json.put("msg","删除失败");
		}
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@RequestMapping("/addProduct")
	public String addProduct(
			Product product, @RequestParam(value = "mainPic",required = true) MultipartFile mainPic,
			@RequestParam(value = "specification[]",required = false) String[] specification,
			HttpServletRequest request, Integer sellerId){
		//如果规格数组无数据
		if(specification==null||specification.length==0){
			product.setDimension("");
		}else {
			String str="";
			for(int i=0;i<specification.length;i++){
				//如果i小于规格数组的长度
				if(i<specification.length-1){
					//将每个规格数组以逗号隔开拼接到str字符串
					str+=specification[i]+",";
				}else{
					str+=specification[i];
				}
			}
			product.setDimension(str);
		}
		String path="";
		String imgPath="";
		String imgPhysicalPath="";
		//获取上传文件的路径
		path = request.getSession().getServletContext().getRealPath("static/uploadfiles" + File.separator + "product");
		String oldFileName = mainPic.getOriginalFilename();//原文件名
		String prefix = FilenameUtils.getExtension(oldFileName);//获取后缀名
		//设置文件名
		String fileName = sellerId+product.getTitle() + "."+prefix;
		//剔除以下字符
		fileName=fileName.replace("*","");
		fileName=fileName.replace("\\","");
		fileName=fileName.replace("/","");
		fileName=fileName.replace(":","");
		fileName=fileName.replace("?","");
		fileName=fileName.replace("\"","");
		fileName=fileName.replace("<","");
		fileName=fileName.replace(">","");
		fileName=fileName.replace("|","");
		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdir();
		}
		try {
			mainPic.transferTo(targetFile);//上传文件
		}catch (IOException e){
			e.printStackTrace();
		}
		imgPath= request.getContextPath() + "/static/uploadfiles/product/" + fileName;//获取文件服务器路径
		imgPhysicalPath = path + File.separator + fileName;//获取文件物理路径
		product.setImgPath(imgPath);
		product.setImgPhysicalPath(imgPhysicalPath);
		//添加商品信息
		productService.addProduct(product);
		return "redirect:/seller/showProduct";
	}
	@RequestMapping("/deleteProduct")
	public String deleteProduct(Integer id,HttpServletRequest request,Model model){
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
		String physicalPath=request.getSession().getServletContext().getRealPath("/");
		Product pro=new Product();
		pro.setId(id);
		//根据id查询商品信息
		Product product=productService.selectProductDetail(pro);
		//根据商品id查询订单信息
		Order order=orderService.queryProductOrderByProduct_id(id);
		//如果查询不到订单信息
		if(order==null){
			List<String> imglist= new ArrayList<>();
			//判断详情是否为空
			if(!product.getDescription().equals("")) {
				//获取商品详情的图片地址列表
				imglist = getImgList.getImgStr(product.getDescription());
				if(imglist.size()>0){
					for (int i = 0; i < imglist.size(); i++) {
						String str = (String) imglist.get(i);
						str = str.replace(basePath, "");
						str = str.replace("/", "\\\\");
						File file = new File(physicalPath + str);
						//删除详情图片信息
						if (file.exists()) {
							file.delete();
						}
					}
				}
				//查询商品主图片物理路径
				String mainPicPath = product.getImgPhysicalPath();
				File file = new File(mainPicPath);
				//删除主图片
				if (file.exists()) {
					file.delete();
				}
				//根据id删除图片信息
				productService.delProduct(id);
				//根据id删除订单信息
				cartService.delByProduct_id(id);
			}
		}
		return "redirect:/seller/showProduct";
	}
	@RequestMapping("/toUpdateProductInfo")
	public String toUpdate(Model model,Integer id){
		Product product=new Product();
		product.setId(id);
		Product pro=productService.selectProductDetail(product);
		List<Product_Type> product_type1=product_typeService.getProductTypes(0);
		List<Product_Type> product_type2=product_typeService.getProductTypes(pro.getType_1());
		List<Product_Type> product_type3=product_typeService.getProductTypes(pro.getType_2());
		model.addAttribute("product",pro);
		model.addAttribute("type_1",product_type1);
		model.addAttribute("type_2",product_type2);
		model.addAttribute("type_3",product_type3);
		return "html/seller/productEdit";
	}
	@RequestMapping("/updateProduct")
	private String update(Product product,
						  @RequestParam(value = "mainPic",required =false) MultipartFile mainPic,
						  @RequestParam(value = "specification[]",required = false) String[] specification,
						  HttpServletRequest request, HttpSession session){
		if(specification==null || specification.length==0){
			product.setDimension("");
		}else {
			String str="";
			for(int i=0;i<specification.length;i++){
				System.out.println("规格"+i+specification[i]);
				if(i<specification.length-1){
					str+=specification[i]+",";
				}else{
					str+=specification[i];
				}
			}
			product.setDimension(str);
		}
		String path="";
		String imgPath="";
		String imgPhysicalPath="";
		if(!mainPic.isEmpty()){
			if(product.getImgPhysicalPath()==null||product.getImgPhysicalPath()==""){

			}else {
				String mainPicPath = product.getImgPhysicalPath();
				File file = new File(mainPicPath);
				if (file.exists()) {
					file.delete();
				}
			}
			Seller seller= (Seller) session.getAttribute("seller");
			path = request.getSession().getServletContext().getRealPath("static/uploadfiles" + File.separator + "product");//获取上传文件的路径
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = seller.getId()+product.getTitle() +df.format(new Date())+".jpg";
			fileName=fileName.replace("*","");
			fileName=fileName.replace("\\","");
			fileName=fileName.replace("/","");
			fileName=fileName.replace(":","");
			fileName=fileName.replace("?","");
			fileName=fileName.replace("\"","");
			fileName=fileName.replace("<","");
			fileName=fileName.replace(">","");
			fileName=fileName.replace("|","");
			File targetFile = new File(path, fileName);
			if (!targetFile.exists()) {
				targetFile.mkdir();
			}
			try {
				mainPic.transferTo(targetFile);//上传文件
				imgPath= request.getContextPath() + "/static/uploadfiles/product/" + fileName;
				imgPhysicalPath = path + File.separator + fileName;
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		if(!imgPath.equals("")){
			product.setImgPath(imgPath);
			product.setImgPhysicalPath(imgPhysicalPath);
		}
		product.setStatus("未审核");
		productService.updateProduct(product);
		return "redirect:/seller/toUpdateProductInfo?id="+product.getId();
	}
	@ResponseBody
	@RequestMapping("/setmsg")
	public HashMap<String,String> setmsg(Integer id, String msg){
		HashMap<String,String> result=new HashMap<>();
		Product product=new Product();
		product.setId(id);
		product.setStatus("未审核");
		product.setMsg(msg);
		//根据id修改商品状态和设置信息
		productService.updateProduct(product);
		String res="success";
		result.put("result",res);
		return result;
	}
}
