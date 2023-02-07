package cn.letao.online_grocery_store.interceptor;

import cn.letao.online_grocery_store.pojo.Buyer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class BuyerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        Buyer buyer=(Buyer) session.getAttribute("buyer");
        if(buyer!=null){
            return true;
        }else{
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                response.setHeader("REDIRECT","REDIRECT");
                response.setHeader("CONTENTPATH","/error/BuyerError");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }else{
                response.sendRedirect( request.getContextPath() +"/error/BuyerError");

            }
            return false;
        }
    }
}
