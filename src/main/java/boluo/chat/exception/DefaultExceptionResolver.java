package boluo.chat.exception;

import boluo.chat.common.CodedException;
import boluo.chat.common.ResVo;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Slf4j
public class DefaultExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {
        log.error(ex.getMessage(), ex);
        res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if(ex instanceof ImValidTokenException) {
            res.setStatus(401);
            write(res, JSONUtil.toJsonStr(ResVo.error(401, ex.getMessage())));
        }else if(ex instanceof CodedException ce) {
            write(res, JSONUtil.toJsonStr(ResVo.error(ce.getCode(), ce.getMessage())));
        }else if(ex instanceof Errors errors) {
            write(res, JSONUtil.toJsonStr(ResVo.error(400, errors.getAllErrors().stream().map(it -> {
                if(it instanceof FieldError fe) {
                    return fe.getField() + ": " + fe.getDefaultMessage();
                }else {
                    return it.getObjectName() + ": " + it.getDefaultMessage();
                }
            }).collect(Collectors.joining(";")))));
        }else {
            write(res, JSONUtil.toJsonStr(ResVo.error(ex.getMessage())));
        }
        return new ModelAndView();
    }

    public void write(HttpServletResponse res, String message) {
        try{
            res.getWriter().write(message);
        }catch (Throwable e) {
            //ignore error
        }
    }

}
