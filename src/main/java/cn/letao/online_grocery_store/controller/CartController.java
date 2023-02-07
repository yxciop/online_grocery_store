package cn.letao.online_grocery_store.controller;

import cn.letao.online_grocery_store.pojo.Buyer;
import cn.letao.online_grocery_store.pojo.Cart;
import cn.letao.online_grocery_store.service.CartService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Resource
    private CartService cartService;

    @ResponseBody
    @RequestMapping("/queryBuyerCart")
    public String queryCart(Integer buyerId){
        JSONObject json=new JSONObject();
        //获取买家的购物车列表信息
        List<Cart> cartList=cartService.selectCartList(buyerId);
        //定义相同店铺归类的购物车列表信息
        List<List<Cart>> cartListList=new ArrayList<List<Cart>>();
        for(int i=0;i<cartList.size();i++){
            //定义相同购物车列表信息
            List<Cart> carts=new ArrayList<Cart>();
            //将购物车信息添加到列表中
            carts.add(cartList.get(i));
            for(int k=i+1;k<cartList.size();k++){
                //判断购物车信息的卖家id是否与其它购物车信息相同
                if(cartList.get(i).getSellerId().equals(cartList.get(k).getSellerId())){
                    //追加相同卖家的购物车信息
                    carts.add(cartList.get(k));
                    i++;
                }
            }
            //添加购物车列表信息
            cartListList.add(carts);
        }
        json.put("cartListList",cartListList);
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/addCart")
    public String addCart(Cart cart, HttpSession session){
        JSONObject json=new JSONObject();
        //判断买家是否有登录
        if(session.getAttribute("buyer")==null){
            json.put("msg","请在主页登录后再加入购物车");
        }else{
            Buyer buyer=(Buyer) session.getAttribute("buyer");
            cart.setBuyerId(buyer.getId());
            //根据商品id,买家id,商品规格查询是否有类似的购物车信息
            Cart c=cartService.selectLikeCart(cart.getProduct_id(),buyer.getId(),cart.getSpecification());
            //如果无类似的购物车信息
            if(c==null){
                //添加该购物车信息
                int i=cartService.addCart(cart);
                if(i==0){
                    json.put("msg","加入失败");
                }else{
                    json.put("msg","加入成功");
                }
            }else{
                //获取购物车数量
                Integer amount=c.getAmount();
                //通过获取购物车原来的购物车数量和新增的购物车数量更改购物车信息
                amount=cart.getAmount()+amount;
                int k=cartService.updateAmount(amount,c.getId());
                if(k==0){
                    json.put("msg","加入失败");
                }else{
                    json.put("msg","加入成功");
                }
            }
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/delete")
    public String deleteCart(Integer id){
        JSONObject json=new JSONObject();
        //根据id删除购物车信息
        int i=cartService.delCart(id);
        if(i==1){
           json.put("msg","删除成功");
        }else {
           json.put("msg","删除失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/updateAmount")
    public String updateAmount(Integer id, Integer amount){
        JSONObject json=new JSONObject();
        //根据id更改购物车数量
        int i=cartService.updateAmount(amount,id);
        if(i==1){
            json.put("msg","更改成功");
        }else {
            json.put("msg","更改失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/updateSpecification")
    public String updateSpecification(String specification,Integer id){
        JSONObject json=new JSONObject();
        //根据id修改规格
        int i=cartService.updateSpecification(specification,id);
        if(i==1){
            json.put("msg","更改成功");
        }else {
            json.put("msg","更改失败");
        }
        return JSONObject.toJSONString(json);
    }
}
