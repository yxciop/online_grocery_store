package cn.letao.online_grocery_store.controller;

import cn.letao.online_grocery_store.pojo.Address;
import cn.letao.online_grocery_store.pojo.Buyer;
import cn.letao.online_grocery_store.service.AddressService;
import cn.letao.online_grocery_store.util.MD5Util;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping( "/address")
public class AddressController {
    @Resource
    private AddressService addressService;

    @RequestMapping("/selectAddress")
    public String selectAddress(HttpSession session,Model model){
       Buyer buyer=(Buyer)session.getAttribute("buyer");
       Integer buyerId=buyer.getId();
       List<Address> address=addressService.queryAddress(buyerId,0);
       for(int i=0;i<address.size();i++){
           address.get(i).setRname(MD5Util.convertMD5(address.get(i).getRname()));
       }
       List<Address> defaultA=addressService.queryAddress(buyerId,1);
       model.addAttribute("addressList",address);
       if(!defaultA.isEmpty()){
           defaultA.get(0).setRname(MD5Util.convertMD5(defaultA.get(0).getRname()));
           model.addAttribute("default",defaultA.get(0));
       }
        return "html/buyer/addressAdmin";
    }
    @ResponseBody
    @RequestMapping("/addAddress")
    public String addAddress(Address address){
        int j=1;
        //判断地址是否是默认地址,1代表默认地址,0普通地址
        if(address.getIsDefault()==null){
            address.setIsDefault(0);
        }else{
            //查询买家的所有默认地址
            if(addressService.queryAddress(address.getBuyerId(),1).size()>0){
                Address otherAddress=new Address();
                otherAddress.setIsDefault(0);
                otherAddress.setBuyerId(address.getBuyerId());
                //更改所有默认地址为普通地址
                j=addressService.updateAddress(otherAddress);
            }
        }
        address.setRname(MD5Util.convertMD5(address.getRname()));
        //添加当前地址
        int i=addressService.addAddress(address);
        JSONObject json=new JSONObject();
        if(i==1 &&j>=1){
            json.put("msg","添加成功");
        }else{
            json.put("msg","添加失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/updateDefault")
    public String updateDefault(Integer id,Integer buyerId){
        Address otherAddress=new Address();
        otherAddress.setIsDefault(0);
        otherAddress.setBuyerId(buyerId);
        //设置所有地址为普通地址
        int j=addressService.updateAddress(otherAddress);
        Address address=new Address();
        address.setIsDefault(1);
        address.setId(id);
        //设置当前地址为默认地址
        int i=addressService.updateAddress(address);
        JSONObject json=new JSONObject();
        if(i==1 && j>=1){
            json.put("msg","设置成功");
        }else{
            json.put("msg","设置失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/delAddress")
    public String delAddress(Integer id,Integer buyerId){
        //删除当前地址
        int i=addressService.delAddress(id);
        //查询普通地址
        List<Address> commonaddress=addressService.queryAddress(buyerId,0);
        //查询默认地址
        List<Address> defaultA=addressService.queryAddress(buyerId,1);
        int j=1;
        //判断有无默认地址
        if(defaultA.size()>0){
        }else{
            //判断有无普通地址
            if(commonaddress.size()>0){
                //将普通地址的第一个地址改为默认地址
                Address address=commonaddress.get(0);
                address.setIsDefault(1);
                j=addressService.updateAddress(address);
            }
        }
        JSONObject json=new JSONObject();
        if(i==1 && j==1){
            json.put("msg","删除成功");
        }else{
            json.put("msg","删除失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/updateAddress")
    public String updateAddress(Address address){
        JSONObject json=new JSONObject();
        //更改收获信息
        address.setRname(MD5Util.convertMD5(address.getRname()));
        int i=addressService.updateAddress(address);
        if(i==1){
            json.put("msg","修改成功");
        }else{
            json.put("msg","修改失败");
        }
        return JSONObject.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping("/addressDetail")
    public String getAddressById(Integer id){
        JSONObject json=new JSONObject();
        Address address=addressService.queryAddressByID(id);
        address.setRname(MD5Util.convertMD5(address.getRname()));
        json.put("address",address);
        return JSONObject.toJSONString(json);
    }
}
