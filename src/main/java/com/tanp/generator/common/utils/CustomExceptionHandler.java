package com.tanp.generator.common.utils;

import com.alibaba.fastjson.JSON;
import com.tanp.generator.common.constant.ResponseResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author CodeBricklayer
 * @date 2019/12/31 15:48
 * @description 异常处理器
 */
@Component
@Slf4j
public class CustomExceptionHandler implements HandlerExceptionResolver {

  @Override
  public ModelAndView resolveException(HttpServletRequest request,
      HttpServletResponse response, Object o, Exception ex) {
    ResponseResult r = new ResponseResult();
    try {
      response.setContentType("application/json;charset=utf-8");
      response.setCharacterEncoding("utf-8");

      if (ex instanceof CustomException) {
        r.put("code", ((CustomException) ex).getCode());
        r.put("msg", ((CustomException) ex).getMessage());
      }else if(ex instanceof DuplicateKeyException){
        r = ResponseResult.error("数据库中已存在该记录");
      }else{
        r = ResponseResult.error();
      }
      //记录异常日志
      log.error(ex.getMessage(), ex);
      String json = JSON.toJSONString(r);
      response.getWriter().print(json);
    } catch (Exception e) {
      log.error("CustomExceptionHandler 异常处理失败", e);
    }
    return new  ModelAndView();
  }
}
