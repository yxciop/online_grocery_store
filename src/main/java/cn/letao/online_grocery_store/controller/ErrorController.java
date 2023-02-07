package cn.letao.online_grocery_store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {
    @RequestMapping("/BuyerError")
    public String buyerError(){
        return "html/error/BuyerError";
    }
    @RequestMapping("/SellerError")
    public String sellerError(){
        return "html/error/SellerError";
    }
    @RequestMapping("/AdminError")
    public String adminError(){
        return "html/error/AdminError";
    }
}
