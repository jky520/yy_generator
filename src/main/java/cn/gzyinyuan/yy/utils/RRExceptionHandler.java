package cn.gzyinyuan.yy.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理器
 * Created by DT人 on 2017/9/7 10:05.
 */
@RestControllerAdvice
@Slf4j
public class RRExceptionHandler {

    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        R r = new R();
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("utf-8");

            if (ex instanceof RRException) {
                r.put("code", ((RRException) ex).getCode());
                r.put("msg", ((RRException) ex).getMessage());
            }else if(ex instanceof DuplicateKeyException){
                r = R.error("数据库中已存在该记录");
            }else{
                r = R.error();
            }

            //记录异常日志
            log.error(ex.getMessage(), ex);

            String json = JSON.toJSONString(r);
            response.getWriter().print(json);
        } catch (Exception e) {
            log.error("RRExceptionHandler 异常处理失败", e);
        }
        return new ModelAndView();
    }
}
