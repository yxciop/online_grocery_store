package cn.letao.online_grocery_store.config;

import cn.letao.online_grocery_store.interceptor.AdminInterceptor;
import cn.letao.online_grocery_store.interceptor.BuyerInterceptor;
import cn.letao.online_grocery_store.interceptor.SellerInterceptor;
import cn.letao.online_grocery_store.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAutoConfiguration
public class MVCconfig implements WebMvcConfigurer {
    @Autowired
    private BuyerInterceptor buyerInterceptor;
    @Autowired
    private SellerInterceptor sellerInterceptor;
    @Autowired
    private AdminInterceptor adminInterceptor;
    @Autowired
    private RoleService roleService;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String buyerAccessAddress=roleService.queryRoleInfo(1).getAccess_to_the_address();
        String sellerAccessAddress=roleService.queryRoleInfo(2).getAccess_to_the_address();
        String adminAccessAddress=roleService.queryRoleInfo(3).getAccess_to_the_address();
        List<String> list1= Arrays.asList(buyerAccessAddress.split(","));
        List<String> list2= Arrays.asList(sellerAccessAddress.split(","));
        List<String> list3= Arrays.asList(adminAccessAddress.split(","));
        registry.addInterceptor(sellerInterceptor)
                .addPathPatterns(list2).excludePathPatterns("/seller/tologin","/seller/login","/seller/register","/seller/tofind","/seller/find","/statics/**");
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns(list3).excludePathPatterns("/admin/tologin","/admin/login","/statics/**");
        registry.addInterceptor(buyerInterceptor)
                 .addPathPatterns(list1).excludePathPatterns("/buyer/login","/buyer/register","/buyer/find","/type/**","/shop/list",
                 "/shop/storeProduct","/shop/showDetail","/statics/**");
    }
}
