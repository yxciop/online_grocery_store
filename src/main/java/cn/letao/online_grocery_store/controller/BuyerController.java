package cn.letao.online_grocery_store.controller;

import cn.letao.online_grocery_store.config.AlipayConfig;
import cn.letao.online_grocery_store.pojo.*;
import cn.letao.online_grocery_store.service.*;
import cn.letao.online_grocery_store.util.KeyUtil;
import cn.letao.online_grocery_store.util.MD5Util;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/buyer")
public class BuyerController {
	@Resource
	private BuyerService buyerService;
	@Resource
	private OrderService orderService;
	@Resource
	private SellerService sellerService;
	@Resource
	private FlowService flowService;
	@Resource
	private ProductService productService;
	@Resource
	private CartService cartService;

	@ResponseBody
	@PostMapping("/login")
	public HashMap<String,String> login(Buyer buyer,HttpSession session){
		HashMap<String,String> result=new HashMap<>();
		//判断买家有无登录
		if(session.getAttribute("buyer")==null){
			//查询有无该用户名的关联用户
			Buyer buyer1=buyerService.queryBuyer(new BuyerVo(buyer.getUsername(),null,null));
			//如果无该
			if(buyer1==null){
				result.put("login_Username_msg","用户名不存在");
			}else{
				//真实密码和输入的密码匹配
				if(!buyer1.getPassword().equals(MD5Util.stringMD5(buyer.getPassword()))){
					result.put("login_Password_msg","密码错误");
				}else{
					//如果正在登录
					if(buyer1.getLoginStatus().equals("login")){
						result.put("login_msg","用户已在别处登陆过,请稍后登录");
					}else{
						buyer1.setLoginStatus("login");
						//更改用户状态
						buyerService.updateBuyer(buyer1);
						result.put("login_msg","登录成功");
						result.put("imgPath",buyer1.getImgPath());
						buyer1.setPassword(buyer.getPassword());
						buyer1.setRealname(MD5Util.convertMD5(buyer1.getRealname()));
						//保存登录信息
						session.setAttribute("buyer",buyer1);
					}
				}
			}
		}
		return result;
	}
	@ResponseBody
	@PostMapping("/find")
	public HashMap<String,String> changePass(Buyer buyer){
		HashMap<String,String> result=new HashMap<>();
		int status=1;
		//查询有无关联用户名的买家用户
		Buyer buyer1=buyerService.queryBuyer(new BuyerVo(buyer.getUsername(),null,null));
		if(buyer1==null){
			status=0;
			result.put("userFail","用户名不存在,无法修改密码");
		}else{
			//判断输入的手机号是否与查询到的买家用户的手机号相等
			if(!buyer.getPhonenumber().equals(buyer1.getPhonenumber())){
				status=0;
				result.put("phoneFail","手机号码信息错误");
			}
			//判断输入的手机号是否与查询到的买家用户的手机号相等
			if(!buyer.getRealname().equals(MD5Util.convertMD5(buyer1.getRealname()))){
				status=0;
				result.put("nameFail","姓名信息错误");
			}
			if (status==1){
				buyer.setId(buyer1.getId());
				buyer.setPassword(MD5Util.stringMD5(buyer.getPassword()));
				//根据买家id更改买家信息
				buyerService.updateBuyer(buyer);
				result.put("change_msg","密码修改成功");
			}
		}
		return result;
	}
	@ResponseBody
	@RequestMapping("/logout")
	public List<String> logout(HttpSession session){
		List<String> list=new ArrayList<>();
		Buyer buyer= (Buyer) session.getAttribute("buyer");
		buyer.setPassword(MD5Util.stringMD5(buyer.getPassword()));
		buyer.setRealname(MD5Util.convertMD5(buyer.getRealname()));
		buyer.setLoginStatus("logout");
		//更改买家登录状态
		buyerService.updateBuyer(buyer);
		//移除买家登录信息
		session.removeAttribute("buyer");
		list.add("退出成功");
		return list;
	}
	@ResponseBody
	@PostMapping("/register")
	public HashMap<String,String> register(Buyer buyer, @RequestParam(value = "pic_path",required = false) MultipartFile pic, HttpServletRequest request) throws Exception{
		HashMap<String,String> result=new HashMap<>();
		String headPicPath = "";
		String headLocPath = "";
		String path="";
		int status=1;
		//根据用户名查询有无相关买家用户
		Buyer buyer1=buyerService.queryBuyer(new BuyerVo(buyer.getUsername(),null,null));
		//根据手机号码查询有无相关买家用户
		Buyer buyer2=buyerService.queryBuyer(new BuyerVo(null,null,buyer.getPhonenumber()));
		if(buyer1!=null){
			status=0;
			result.put("username_msg","用户名已存在");
		}
		if(buyer2!=null){
			status=0;
			result.put("phone_msg","手机号码已注册过其他账号");
		}
		if(buyer1==null&&buyer2==null){
			//判断文件是否为空
			if(!pic.isEmpty()){
				//根据上下文获取上传图片的物理路径
				path=request.getSession().getServletContext().getRealPath("static/uploadfiles" + File.separator + "buyerHead");
				String oldFileName = pic.getOriginalFilename();//原文件名
				String prefix = FilenameUtils.getExtension(oldFileName);//获取后缀名
				//设置上传文件名
				String fileName = buyer.getUsername() + "买家头像."+prefix;
				//剔除以下这些字符
				fileName=fileName.replace("*","");
				fileName=fileName.replace("\\","");
				fileName=fileName.replace("/","");
				fileName=fileName.replace(":","");
				fileName=fileName.replace("?","");
				fileName=fileName.replace("\"","");
				fileName=fileName.replace("<","");
				fileName=fileName.replace(">","");
				fileName=fileName.replace("|","");
				//创建目标文件对象
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
				headPicPath = request.getContextPath() + "/static/uploadfiles/buyerHead/" + fileName;
				headLocPath = path + File.separator + fileName;
			}
		}
		if(status==1){
			buyer.setImgPath(headPicPath);
			buyer.setImgPhysicalPath(headLocPath);
			buyer.setPassword(MD5Util.stringMD5(buyer.getPassword()));
			buyer.setRealname(MD5Util.convertMD5(buyer.getRealname()));
			//添加买家信息
			buyerService.register(buyer);
			result.put("reg_msg", "注册成功");
		}
		return result;
	}
	@ResponseBody
	@PostMapping("/updateInfo")
	public HashMap<String,String> update(
			Buyer buyer, @RequestParam(value = "pic_path",required = false) MultipartFile pic,
			HttpServletRequest request, HttpSession session) throws Exception{
		HashMap<String,String> result=new HashMap<>();
		String path="";
		String headPicPath = "";
		String headLocPath = "";
		//获取session保存的买家信息
		Buyer buyer1=(Buyer) session.getAttribute("buyer");
		int status=1;
		//查询与相关手机号的买家用户
		Buyer buyer2=buyerService.queryBuyer(new BuyerVo(null,null,buyer.getPhonenumber()));
		if(buyer2!=null){
			if(!buyer.getPhonenumber().equals(buyer2.getPhonenumber())){
				status=0;
				result.put("phoneNumberMsg","手机号码已注册过其他账号");
			}
		}
		//根据上下文获取上传图片的物理路径
		path = request.getSession().getServletContext().getRealPath("static/uploadfiles" + File.separator + "buyerHead");
		if(!pic.isEmpty()) {
			if(status==1){
				if(buyer1.getImgPhysicalPath()!=null){
					//获取买家头像的物理路经
					String buyerImgPath = buyer1.getImgPhysicalPath();
					//找到该文件并删除
					File file = new File(buyerImgPath);
					if (file.exists()) {
						file.delete();
					}
				}
				//定义时间格式年月日时分秒
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String oldFileName = pic.getOriginalFilename();//原文件名
				String prefix = FilenameUtils.getExtension(oldFileName);//获取后缀名
				//设置文件名
				String fileName = buyer1.getId()+buyer1.getUsername() +df.format(new Date())+"买家头像."+prefix;
				//剔除以下这些字符
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
				//判断目标目录是否存在
				if (!targetFile.exists()) {
					targetFile.mkdir();
				}
				try {
					pic.transferTo(targetFile);//上传文件
				}catch (IOException e){
					e.printStackTrace();
				}
				headPicPath = request.getContextPath() + "/static/uploadfiles/buyerHead/" + fileName;
				headLocPath = path + File.separator + fileName;
			}
		}
		if(status==1){
			buyer.setId(buyer1.getId());
			buyer.setImgPath(headPicPath);
			buyer.setImgPhysicalPath(headLocPath);
			buyer1.setPassword(buyer.getPassword());
			buyer.setPassword(MD5Util.stringMD5(buyer.getPassword()));
			//更改买家信息
			buyerService.updateBuyer(buyer);
			result.put("update_msg", "修改成功");
			//修改保存的买家信息
			buyer1.setNickname(buyer.getNickname());
			buyer1.setPhonenumber(buyer.getPhonenumber());
			if(!headPicPath.equals("")){
				buyer1.setImgPath(headPicPath);
				buyer1.setImgPhysicalPath(headLocPath);
			}
			session.setAttribute("buyer",buyer1);
		}
		return result;
	}
	@RequestMapping("/orderAdmin")
	public String toOrderAdmin(){
		return "html/buyer/orderAdmin";
	}
	@RequestMapping("/cartAdmin")
	public String showCart(Model model, HttpSession session){
		if(session.getAttribute("buyer")==null){
			model.addAttribute("msg","请在主页登录后再加入购物车");
			return "redirect:/shop/list";
		}else{
			return "html/buyer/cartAdmin";
		}
	}
	@ResponseBody
	@PostMapping("/complainOrder")
	public String complain(Integer id,String oid,Integer sellerId,String complain){
		JSONObject json=new JSONObject();
		Order order=new Order();
		order.setId(id);
		order.setStatus("退款中");
		order.setMsg("买家:订单异常,请卖家同意退款-------"+"投诉原因:"+complain);
		//根据id修改订单状态和订单投诉信息
		int i=orderService.updateOrder(order);
		Seller seller=new Seller();
		seller.setId(sellerId);
		seller.setStatus("异常");
		seller.setMsg("订单"+oid+"的买家投诉当前用户,请到订单管理中心处理");
		//根据id更改卖家状态为异常并且更改投诉信息
		int k=sellerService.updateSeller(seller);
		if(i==1&&k==1){
			json.put("msg","投诉成功");
		}else{
			json.put("msg","投诉失败");
		}
		return JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/cancelComplain")
	public String cancel(Integer oid,Integer sellerId){
		JSONObject json=new JSONObject();
		Order order=new Order();
		order.setId(oid);
		order.setMsg("");
		//清除订单投诉信息为空
		int i=orderService.updateOrder(order);
		Seller seller=new Seller();
		seller.setId(sellerId);
		seller.setStatus("正常");
		seller.setMsg("帐户恢复正常");
		//让卖家帐号恢复正常
		int k=sellerService.updateSeller(seller);
		if(i==1&&k==1){
			json.put("msg","cancelSuccess");
		}else{
			json.put("msg","cancelFail");
		}
		return JSONObject.toJSONString(json);
	}
	@ResponseBody
	@PostMapping(value="/buy",produces = "text/html; charset=UTF-8")
	public String buy(@RequestParam(required = false) String  idArray,
					  @RequestParam(required = false) Integer productid,
					  @RequestParam(required = false) Integer id,
					  @RequestParam(required = false,defaultValue = "")String specification,
					  @RequestParam(required = false) Integer amount,
					  @RequestParam(required = false,defaultValue = "") String  status,
					  String recipient, String phonenumber,
					  String address,Integer buyerId) throws Exception {
		String orderid= KeyUtil.genUniqueKey();
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = orderid;
		//付款金额，必填
		String total_amount ="";
		//订单名称，必填
		String subject ="";
		//商品描述，可空
		String body = "用户订购商品个数：";
		// 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
		String timeout_express = "1h";
		Double totalPrice=0.00;
		String result ="";
		if(idArray.length()==0){
			Product product=new Product();
			product.setId(productid);
			product=productService.selectProductDetail(product);
			Order order=new Order();
			order.setOrderId(orderid);
			order.setPrice(product.getPrice());
			order.setProductname(product.getTitle());
			order.setBuyerId(buyerId);
			order.setSellerId(product.getSellerId());
			order.setPhonenumber(phonenumber);
			order.setAddress(address);
			order.setStatus("未支付");
			order.setRecipient(MD5Util.convertMD5(recipient));
			order.setSpecification(specification);
			order.setAmount(amount);
			order.setProduct_id(productid);
			order.setTotal_price(product.getPrice()*amount);
			System.out.println(order.toString());
			DecimalFormat df=new DecimalFormat("#.00");
			if(!status.equals("未支付")){
				orderService.addOrder(order);
			}else{
				orderService.updateOrderId(orderid,id);
			}
			total_amount = df.format(product.getPrice()*amount);
			//订单名称，必填
			subject =product.getTitle();
			//商品描述，可空
			body = "用户订购商品个数："+1;
			alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
					+ "\"total_amount\":\""+ total_amount +"\","
					+ "\"subject\":\""+ subject +"\","
					+ "\"body\":\""+ body +"\","
					+ "\"timeout_express\":\""+ timeout_express +"\","
					+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
			//请求
			result = alipayClient.pageExecute(alipayRequest).getBody();
		}else{
			Integer[] ids= JSONObject.parseArray(idArray).toArray(new Integer[0]);
			List<Cart> cartLists=cartService.ByArrayQueryCartList(ids);
			for(int i=0;i<cartLists.size();i++){
				cartService.delCart(cartLists.get(i).getId());
			}
			List<Order> orderList=new ArrayList<Order>();
			for(Cart cart:cartLists){
				Order order=new Order();
				order.setOrderId(orderid);
				order.setPrice(cart.getPrice());
				order.setProductname(cart.getTitle());
				order.setBuyerId(buyerId);
				order.setSellerId(cart.getSellerId());
				order.setPhonenumber(phonenumber);
				order.setAddress(address);
				order.setStatus("未支付");
				order.setRecipient(MD5Util.convertMD5(recipient));
				order.setSpecification(cart.getSpecification());
				order.setAmount(cart.getAmount());
				order.setProduct_id(cart.getProduct_id());
				order.setTotal_price(cart.getPrice()*cart.getAmount());
				orderList.add(order);
				totalPrice+=cart.getPrice()*cart.getAmount();
			}
			orderService.insertOrders(orderList);
			DecimalFormat df=new DecimalFormat("#.00");
			//付款金额，必填
			total_amount = df.format(totalPrice);
			//订单名称，必填
			subject =orderList.size()+"种商品测试";
			//商品描述，可空
			body = "用户订购商品个数："+1;
			alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
					+ "\"total_amount\":\""+ total_amount +"\","
					+ "\"subject\":\""+ subject +"\","
					+ "\"body\":\""+ body +"\","
					+ "\"timeout_express\":\""+ timeout_express +"\","
					+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
			//请求
			result = alipayClient.pageExecute(alipayRequest).getBody();

		}
		return result;
	}
	@RequestMapping(value = "/alipayReturnNotice")
	public String alipayReturnNotice(HttpServletRequest request, ModelMap model) throws Exception {
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
		//——请在这里编写您的程序（以下代码仅作参考）——
		if(signVerified) {
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
			//修改叮当状态，改为 支付成功，已付款;
			Order order=new Order();
			order.setOrderId(out_trade_no);
			List<Order> orderList=orderService.queryBuyerOrders(order);
			for(int k=0;k<orderList.size();k++){
				Product product=new Product();
				product.setId(orderList.get(k).getProduct_id());
				productService.subtractStock(orderList.get(k).getAmount(),orderList.get(k).getProduct_id());
				product.setSoldCount(orderList.get(k).getAmount());
				productService.updateProduct(product);
			}
			order.setStatus("未发货");
			System.out.println(order.toString());
			orderService.updateOrder(order);
			//同时新增支付流水
			Flow flow = new Flow();
			String flowid=KeyUtil.genUniqueKey();
			flow.setId(flowid);
			flow.setTrade_no(trade_no);
			flow.setOut_trade_no(out_trade_no);
			flow.setPaid_amount(Double.parseDouble(total_amount));
			flow.setCreated_time(new Date());
			flowService.insert(flow);

			model.put("out_trade_no",out_trade_no);//订单编号
			model.put("trade_no",trade_no);//支付宝交易号
			model.put("total_amount",total_amount);//实付金额
		}else {
			System.out.println("支付, 验签失败...");
		}
		return "html/shop/alipaySuccess";
	}
	@RequestMapping(value = "/alipayNotifyNotice")
	@ResponseBody
	public String alipayNotifyNotice(HttpServletRequest request) throws Exception {

		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

		//——请在这里编写您的程序（以下代码仅作参考）——

		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
		if(signVerified) {//验证成功
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

			//付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

			if(trade_status.equals("TRADE_FINISHED")){

			}else if (trade_status.equals("TRADE_SUCCESS")){

			}
			System.out.println("支付成功...");

		}else {//验证失败
			System.out.println("支付, 验签失败...");
		}

		return "success";
	}
	@ResponseBody
	@RequestMapping("/queryBuyerOrder")
	public String queryOrders(Integer buyerId){
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		Order order=new Order();
		order.setBuyerId(buyerId);
		//根据买家id查询订单列表信息
		List<Order> orderList=orderService.queryBuyerOrders(order);
		//定义根据下单时间分类的订单列表
		List<List<Order>> orderListList=new ArrayList<List<Order>>();
		for(int i=0;i<orderList.size();i++){
			List<Order> orders=new ArrayList<Order>();
			//添加订单列表信息
			orders.add(orderList.get(i));
			for(int k=i+1;k<orderList.size();k++){
				//判断当前订单创建时间是否与其他订单创建时间相同
				if(orderList.get(i).getCreatedTime().equals(orderList.get(k).getCreatedTime())){
					Order order1=orderList.get(k);
					order1.setRecipient(MD5Util.convertMD5(order1.getRecipient()));
					//添加订单列表信息
					orders.add(order1);
					i++;
				}
			}
			orderListList.add(orders);
		}
		json.put("orderListList",orderListList);
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/refundOrder")
	public String refundOrder(Integer id,String reason){
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		Order order=new Order();
		order.setId(id);
		order.setStatus("退款中");
		order.setMsg(reason);
		//根据id更改订单状态并设置退款原因
		int i=orderService.updateOrder(order);
		if(i==1){
			json.put("msg","申请成功");
		}else{
			json.put("msg","申请失败");
		}
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/updateOrderInfo")
	public String updateReceive(Order order){
		order.setRecipient(MD5Util.convertMD5(order.getRecipient()));
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		//修改订单信息
		int i=orderService.updateOrder(order);
		if(i==1){
			json.put("msg","修改成功");
		}else{
			json.put("msg","修改失败");
		}
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/deleteBuyerOrder")
	public String deleteBuyerOrder(Integer id){
		int i=0;
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		Order order=new Order();
		order.setId(id);
		//根据id查询订单详情
		Order order1=orderService.queryBuyerOrders(order).get(0);
		//如果卖家id为0
		if(order1.getSellerId()==0){
			//删除订单信息
			i=orderService.deleteOrder(id);
		}else{
			//设置买家id为0
			order.setBuyerId(0);
			//更改订单信息的买家id
			i=orderService.updateOrder(order);
		}
		if(i==1){
			json.put("msg","删除成功");
		}else{
			json.put("msg","删除失败");
		}
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
	@ResponseBody
	@RequestMapping("/confirmReceive")
	public String receive(Integer id){
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		Order order=new Order();
		order.setId(id);
		order.setStatus("已收货");
		order.setMsg("");
		//根据id更改订电脑状态和设置订单信息
		int i=orderService.updateOrder(order);
		if(i==1){
			json.put("msg","确认成功");
		}else{
			json.put("msg","确认失败");
		}
		return com.alibaba.fastjson.JSONObject.toJSONString(json);
	}
}
