package com.mmall.controller.common.interceptor;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * 拦截器
 *
 * @author chencong
 */

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    /**
     * 进入controller之前
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        /*请求中的方法名*/
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        /*解析具体参数，key value*/
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = request.getParameterMap();
        /*对paramMap进行迭代*/
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String mapKey = (String) entry.getKey();
            String mapValue = StringUtils.EMPTY;

            /*request 这个参数的map返回的是一个String[] 数组*/
            Object obj = entry.getValue();
            if (obj instanceof String[]) {
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        /*判断用户是否登录*/
        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr, User.class);
        }
        if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)) {
            /*返回false 不会调用controller*/
            /*对数输入进行拦截*/
            /*这里要添加reset 否则会报异常 getWriter() has already been called for this response */
            response.reset();

            /*重写response 是需要添加上返回编码*/
            response.setCharacterEncoding("UTF-8");

            /*返回json response 接管在拦截器当中*/
            response.setContentType("application/json;charset=UTF-8");

            /*进行输出*/
            PrintWriter out = response.getWriter();

            /*细化业务逻辑*/
            if (user == null) {
                out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
            } else {
                out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限操作")));
            }

            out.flush();
            /*关闭输出流*/
            out.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        /*视图执行完成之后*/
        log.info("afterCompletion");
    }
}
