package cn.letao.online_grocery_store.interceptor;

import cn.letao.online_grocery_store.pojo.Admin;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        Admin admin=(Admin) session.getAttribute("admin");
        if(admin!=null){
            return true;
        }else{
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                response.setHeader("REDIRECT","REDIRECT");
                response.setHeader("CONTENTPATH","/error/AdminError");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }else{
                response.sendRedirect( request.getContextPath() +"/error/AdminError");

            }
            return false;
        }
    }
}
