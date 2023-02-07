package cn.letao.online_grocery_store.controller;

import cn.letao.online_grocery_store.config.AlipayConfig;
import cn.letao.online_grocery_store.pojo.*;
import cn.letao.online_grocery_store.service.FlowService;
import cn.letao.online_grocery_store.service.OrderService;
import cn.letao.online_grocery_store.service.ProductService;
import cn.letao.online_grocery_store.util.KeyUtil;
import cn.letao.online_grocery_store.util.MD5Util;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.math.BigDecimal;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;
    @Resource
    private FlowService flowService;
    @Resource
    private ProductService productService;


    @ResponseBody
    @PostMapping(value="/refund",produces = "text/html; charset=UTF-8")
    public String refund(
            @RequestParam(required = false,defaultValue = "") String  denyOrder,
            String orderId, String total_price, Integer productId,Integer id) throws AlipayApiException {

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        //设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = orderId;
        //根据订单号查询订单流水信息
        Flow flow=flowService.selectByPrimaryKey(orderId);
        //支付宝交易号
        String trade_no = flow.getTrade_no();
        //请二选一设置
        //需要退款的金额，该金额不能大于订单金额，必填
        String refund_amount = total_price;
        //退款的原因说明
        String refund_reason = "退款原因";
        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
        String out_request_no = flow.getId();
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"trade_no\":\""+ trade_no +"\","
                + "\"refund_amount\":\""+ refund_amount +"\","
                + "\"refund_reason\":\""+ refund_reason +"\","
                + "\"out_request_no\":\""+ out_request_no +"\"}");
        //请求
        String result = alipayClient.execute(alipayRequest).getBody();
        JSONObject jsonObject= new JSONObject(result);
        JSONObject alipay_trade_refund_response=jsonObject.getJSONObject("alipay_trade_refund_response");
        String  code=alipay_trade_refund_response.getString("code");
        if(code.equals("10000")){
            Double total=Double.valueOf(total_price.toString());
            //如果所剩金额为0
            if(minus(flow.getPaid_amount(),total)==0.00){
                //根据订单号删除流水信息
                flowService.deleteByPrimaryKey(out_trade_no);
            }else{
                //设置所剩金额
                flow.setPaid_amount(minus(flow.getPaid_amount(),total));
                //设置新的流水单号
                flow.setId(KeyUtil.genUniqueKey());
                //更改流水信息
                flowService.updateByPrimaryKey(flow);
            }
            Product pro=new Product();
            pro.setId(productId);
            //根据商品id查询商品信息
            Product product=productService.selectProductDetail(pro);
            //获取商品库存
            Integer stock=product.getStock();
            //根据id查询订单的购买量
            Integer amount=orderService.queryOrderDetailById(id).getAmount();
            //库存与购买量增加
            stock=stock+amount;
            //重新设置商品库存
            pro.setStock(stock);
            //更改商品信息
            productService.updateProduct(pro);
            Order order=new Order();
            order.setId(id);
            //如果卖家没有拒绝订单
            if(denyOrder.equals("")){
                order.setStatus("退款成功");
                order.setMsg("退款已到帐,请查看帐户退款信息");
                //根据id更改订单状态和信息
                orderService.updateOrder(order);
            }else{
                order.setStatus("卖家拒绝订单");
                order.setMsg("退款已打到帐户上,请查看");
                //根据id更改订单状态和信息
                orderService.updateOrder(order);
            }
        }
        return result;
    }
    @ResponseBody
    @RequestMapping("/orderDetail")
    public String getOrderByID(Integer id){
        com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
        //根据id查询订单信息,并放到json对象里
        Order order=new Order();
        order.setId(id);
        Order order1=orderService.queryBuyerOrders(order).get(0);
        order1.setRecipient(MD5Util.convertMD5(order1.getRecipient()));
        json.put("order",order1);
        return com.alibaba.fastjson.JSONObject.toJSONString(json);
    }
    public static Double minus(Double v1,Double v2){
        BigDecimal b1=new BigDecimal(v1.toString());
        BigDecimal b2=new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }
}
