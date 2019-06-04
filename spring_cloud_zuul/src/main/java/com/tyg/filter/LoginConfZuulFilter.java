package com.tyg.filter;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginConfZuulFilter extends ZuulFilter {

    @Override//过滤器类型
    public String filterType() {
        return "pre";
    }

    @Override//过滤器顺序
    public int filterOrder() {
        return 0;
    }

    @Override//该过滤器是否需要执行
    public boolean shouldFilter() {
        return true;
    }

    @Override//编写业务逻辑
    public Object run() {

        RequestContext request1 = RequestContext.getCurrentContext();
        HttpServletRequest request = request1.getRequest();
        String token = request.getParameter("token");
        if(StringUtils.isEmpty(token)){
            request1.setSendZuulResponse(false);
            request1.setResponseStatusCode(401);
            return null;
        }

        return null;
    }
}
