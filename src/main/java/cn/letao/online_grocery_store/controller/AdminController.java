package cn.letao.online_grocery_store.controller;

import cn.letao.online_grocery_store.pojo.*;
import cn.letao.online_grocery_store.service.*;
import cn.letao.online_grocery_store.util.MD5Util;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private ProductService productService;
    @Resource
    private Product_TypeService product_typeService;
    @Resource
    private BuyerService buyerService;
    @Resource
    private OrderService orderService;
    @Resource
    private SellerService sellerService;

    @RequestMapping("/tologin")
    public String toLogin(){
        return "jsp/admin/login";
    }
    @RequestMapping("/login")
    public String  login(String username, String password, HttpSession session, Model model){
        List<Product> productList=new ArrayList<>();
        if(session.getAttribute("admin")==null){
            Admin admin=new Admin();
            admin.setUsername(username);
            admin.setPassword(password);
            Admin queryAdmin=adminService.queryAdmin(new Admin(username,null));
            if(queryAdmin==null){
                model.addAttribute("usernameMsg","用户名不存在");
            }else{
                if(!queryAdmin.getPassword().equals(MD5Util.stringMD5(password))){
                    model.addAttribute("passwordMsg","密码错误");
                }else if(queryAdmin.getLoginStatus()=="login"){
                    model.addAttribute("Msg","用户已在别的地方登录");
                }else {
                    queryAdmin.setLoginStatus("login");
                    adminService.updateStatus(queryAdmin);
                    productList=productService. successUnreviewedProducts(10);
                    model.addAttribute("products",productList);
                    session.setAttribute("admin",queryAdmin);
                    return "jsp/admin/success";
                }
            }
        }else{
            productList=productService. successUnreviewedProducts(10);
            model.addAttribute("products",productList);
            return "jsp/admin/success";
        }
        return "jsp/admin/login";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        Admin admin= (Admin) session.getAttribute("admin");
        admin.setLoginStatus("logout");
        adminService.updateStatus(admin);
        session.removeAttribute("admin");
        return "jsp/admin/login";
    }
    @RequestMapping("/addProductType")
    public String toAddProductType(Model model){
        List<Product_Type> types=product_typeService.getProductTypes(0);
        System.out.println(types.get(1).getStype());
        model.addAttribute("types",types);
        return "jsp/admin/addProduct_Type";
    }
    @RequestMapping("/delProductType")
    public String todelProductType(){
        return "jsp/admin/delProduct_Type";
    }
    @RequestMapping("/checkProduct")
    public String toCheckProduct(){
        return "jsp/admin/checkProduct";
    }
    @RequestMapping("/checkOrder")
    public String toCheckOrder(){
        return "jsp/admin/checkOrder";
    }
    @RequestMapping("/checkBuyer")
    public String tocheckBuyer(){
        return "jsp/admin/abnormalBuyerAdmin";
    }
    @RequestMapping("/checkSeller")
    public String tocheckSeller(){
        return "jsp/admin/abnormalSellerAdmin";
    }
    @ResponseBody
    @RequestMapping("/queryAbnormalBuyer")
    public String queryAbnBuyer(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,
                                @RequestParam(defaultValue = "10",value = "pageSize") Integer pageSize){
        JSONObject json=new JSONObject();
        List<Buyer> buyerList=buyerService.queryAbnormalUser(pageNum,pageSize);
        PageInfo<Buyer> pageInfo=new PageInfo<>(buyerList);
        json.put("pageInfo",pageInfo);
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/warnBuyer")
    public String warnBuyer(Integer buyerId){
        JSONObject json=new JSONObject();
        Buyer buyer=new Buyer();
        buyer.setId(buyerId);
        buyer.setStatus("警告");
        String msg=buyerService.selectBuyerMsg(buyerId);
        msg+="<br><b>管理员已投诉此用户,请赶紧处理相应订单"+new Date()+"</b>";
        buyer.setMsg(msg);
        int i=buyerService.updateBuyer(buyer);
        if(i==1){
            json.put("msg","警告成功");
        }else{
            json.put("msg","警告失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/recoverBuyer")
    public String recoverBuyer(Integer buyerId){
        JSONObject json=new JSONObject();
        Buyer buyer=new Buyer();
        buyer.setId(buyerId);
        buyer.setStatus("正常");
        buyer.setMsg("");
        int i=buyerService.updateBuyer(buyer);
        if(i==1){
            json.put("msg","恢复成功");
        }else{
            json.put("msg","恢复失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/suspendBuyer")
    public String suspendBuyer(Integer buyerId){
        JSONObject json=new JSONObject();
        Buyer buyer=new Buyer();
        buyer.setId(buyerId);
        buyer.setStatus("封号");
        String msg=buyerService.selectBuyerMsg(buyerId);
        msg+="<br><b>管理员已对你封号"+new Date()+"</b>";
        buyer.setMsg(msg);
        int i=buyerService.updateBuyer(buyer);
        if(i==1){
            json.put("msg","封号成功");
        }else{
            json.put("msg","警告失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/resolveBuyer")
    public String resolveBuyer(Integer buyerId){
        JSONObject json=new JSONObject();
        Buyer buyer=new Buyer();
        buyer.setId(buyerId);
        buyer.setStatus("正常");
        buyer.setMsg("");
        int i=buyerService.updateBuyer(buyer);
        if(i==1){
            json.put("msg","解封成功");
        }else{
            json.put("msg","解封失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/urgeRefund")
    public String urgeRefund(Integer id){
        com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
        Order order=new Order();
        order.setId(id);
        order.setStatus("催促退款");
        String msg=orderService.queryOrderDetailById(id).getMsg();
        order.setMsg(msg+"<br><p style='colo:red'>管理员已介入退款，请卖家申请退款</p>");
        int i=orderService.updateOrder(order);
        if(i==1){
            json.put("msg","催促成功");
        }else{
            json.put("msg","催促失败");
        }
        return com.alibaba.fastjson.JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/urgeSend")
    public String urgeSend(Integer id){
        com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
        Order order=new Order();
        order.setId(id);
        String msg=orderService.queryOrderDetailById(id).getMsg();
        order.setMsg(msg+"<br><p style='colo:red'>请卖家抓紧发货</p>");
        int i=orderService.updateOrder(order);
        if(i==1){
            json.put("msg","催促成功");
        }else{
            json.put("msg","催促失败");
        }
        return com.alibaba.fastjson.JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/selectUndisposedOrder")
    public String queryUndisposedOrder(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,
                                       @RequestParam(defaultValue = "8",value = "pageSize") Integer pageSize){
        com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
        List<Order> orderList=orderService.selectUndisposedOrder(pageNum,pageSize);
        PageInfo<Order> pageInfo=new PageInfo<>(orderList);
        json.put("pageInfo",pageInfo);
        return com.alibaba.fastjson.JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/addType1")
    public String addProductType1(String stype,Integer idety){
        Product_Type product_type=new Product_Type();
        product_type.setParentId(0);
        product_type.setStype(stype.trim());
        product_type.setIdety(idety);
        product_typeService.addProductType(product_type);
        JSONObject json=new JSONObject();
        json.put("msg","addsuccess");
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/addType2||3")
    public String addProductType2(String stype,Integer idety,Integer parentId){
        Product_Type parentType=new Product_Type();
        parentType.setId(parentId);
        parentType.setSoncode(1);
        product_typeService.updateProductTypeSoncode(parentType);
        Product_Type product_type=new Product_Type();
        product_type.setParentId(parentId);
        product_type.setStype(stype.trim());
        product_type.setIdety(idety);
        product_typeService.addProductType(product_type);
        JSONObject json=new JSONObject();
        json.put("msg","addsuccess");
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/queryTypeList")
    public String queryProductType(@RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum,
                                   @RequestParam(defaultValue = "6",value = "pageSize") Integer pageSize){
        List<Product_Type> list=product_typeService.getTypesDetail(pageNum,pageSize);
        List<Product_Type> parentList=product_typeService.getProductTypes(0);
        PageInfo<Product_Type> pageInfo=new PageInfo<>(list);
        JSONObject json=new JSONObject();
        json.put("pageInfo",pageInfo);
        json.put("parentList",parentList);
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/delType")
    public String delProductType(Integer id,Integer parentId,Integer soncode){
        JSONObject json=new JSONObject();
        if(soncode==0){
            Product product1=new Product();
            product1.setType_2(id);
            Product product2=new Product();
            product2.setType_3(id);
            if(productService.selectProductDetail(product1)!=null||productService.selectProductDetail(product2)!=null){
                json.put("msg","该分类有关联其他商品不能删除");
            }else{
                product_typeService.deleteType(id);
                List<Product_Type> brother_types=product_typeService.getProductTypes(parentId);
                if(brother_types.size()==0){
                    Product_Type product_type=new Product_Type();
                    product_type.setId(parentId);
                    product_type.setSoncode(0);
                    product_typeService.updateProductTypeSoncode(product_type);
                }
                json.put("msg","删除成功");
            }
        }else{
            Product pro=new Product();
            pro.setType_2(id);
            if(productService.selectProductDetail(pro)!=null){
                json.put("msg","该分类有关联其他商品不能删除");
            }else{
                List<Product_Type> product_types=product_typeService.getProductTypes(id);
                for(int i=0;i<product_types.size();i++){
                    product_typeService.deleteType(product_types.get(i).getId());
                }
                product_typeService.deleteType(id);
                List<Product_Type> brother_types=product_typeService.getProductTypes(parentId);
                if(brother_types.size()==0){
                    Product_Type product_type=new Product_Type();
                    product_type.setId(parentId);
                    product_type.setSoncode(0);
                    product_typeService.updateProductTypeSoncode(product_type);
                }
                json.put("msg","删除成功");
            }
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/showProductList")
    public String showNotCheckProduct(
            @RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum,
            @RequestParam(defaultValue = "5",value = "pageSize") Integer pageSize){
        List<Product> productList=productService.selectUnreviewedProducts(pageNum,pageSize);
        PageInfo<Product> pageInfo=new PageInfo<>(productList);
        JSONObject json=new JSONObject();
        json.put("pageInfo",pageInfo);
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/updateProductStatus")
    public String updateProductStatus(Integer id){
        Product product=new Product();
        product.setId(id);
        product.setStatus("审核过");
        product.setMsg("");
        int i=productService.updateProduct(product);
        JSONObject json=new JSONObject();
        if(i==1){
            json.put("msg","审核通过");
        }else {
            json.put("msg","审核失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/setProductMsg")
    public String setProductMsg(Integer id,String msg){
        Product product=new Product();
        product.setId(id);
        product.setMsg(msg);
        product.setStatus("不通过");
        int i=productService.updateProduct(product);
        JSONObject json=new JSONObject();
        if(i==1){
            json.put("msg","发送原因成功");
        }else {
            json.put("msg","发送原因失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/queryDetail")
    public String showDetails(Integer id){
        JSONObject json=new JSONObject();
        Product pro=new Product();
        pro.setId(id);
        Product product=productService.selectProductDetail(pro);
        String firstType=product_typeService.queryTypeById(product.getType_1());
        String secondType=product_typeService.queryTypeById(product.getType_2());
        String thirdType=product_typeService.queryTypeById(product.getType_3());
        json.put("firstType",firstType);
        json.put("secondType",secondType);
        json.put("thirdType",thirdType);
        json.put("product",product);
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/queryAbnormalSeller")
    public String queryAbnSeller(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,
                                 @RequestParam(defaultValue = "10",value = "pageSize") Integer pageSize){
        JSONObject json=new JSONObject();
        List<Seller> sellerList=sellerService.queryAbnormalSeller(pageNum,pageSize);
        PageInfo<Seller> pageInfo=new PageInfo<>(sellerList);
        json.put("pageInfo",pageInfo);
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/warnSeller")
    public String warnSeller(Integer sellerId){
        JSONObject json=new JSONObject();
        Seller seller=new Seller();
        seller.setId(sellerId);
        seller.setStatus("警告");
        String msg=sellerService.selectSellerMsg(sellerId);
        msg+="<br><b>管理员已投诉此用户,请赶紧处理相应订单"+new Date()+"</b>";
        seller.setMsg(msg);
        int i=sellerService.updateSeller(seller);
        if(i==1){
            json.put("msg","警告成功");
        }else{
            json.put("msg","警告失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/recoverSeller")
    public String recoverSeller(Integer sellerId){
        JSONObject json=new JSONObject();
        Seller seller=new Seller();
        seller.setId(sellerId);
        seller.setStatus("正常");
        seller.setMsg("");
        int i=sellerService.updateSeller(seller);
        if(i==1){
            json.put("msg","恢复成功");
        }else{
            json.put("msg","恢复失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/suspendSeller")
    public String suspendSeller(Integer sellerId){
        JSONObject json=new JSONObject();
        Seller seller=new Seller();
        seller.setId(sellerId);
        seller.setStatus("封号");
        String msg=sellerService.selectSellerMsg(sellerId);
        msg+="<br><b>管理员已对你封号"+new Date()+"</b>";
        seller.setMsg(msg);
        int i=sellerService.updateSeller(seller);
        if(i==1){
            json.put("msg","封号成功");
        }else{
            json.put("msg","警告失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/resolveSeller")
    public String resolveSeller(Integer sellerId){
        JSONObject json=new JSONObject();
        Seller seller=new Seller();
        seller.setId(sellerId);
        seller.setStatus("正常");
        seller.setMsg("");
        int i=sellerService.updateSeller(seller);
        if(i==1){
            json.put("msg","解封成功");
        }else{
            json.put("msg","解封失败");
        }
        return JSONObject.toJSONString(json);
    }
}
