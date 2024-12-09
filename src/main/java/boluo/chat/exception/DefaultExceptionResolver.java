package boluo.chat.exception;

import boluo.chat.common.CodedException;
import boluo.chat.common.ResVo;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class DefaultExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {
        log.error(ex.getMessage(), ex);
        res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if(ex instanceof ImValidTokenException) {
            try{
                res.setStatus(401);
                res.getWriter().write(JSONUtil.toJsonStr(ResVo.error(401, ex.getMessage())));
            }catch (Throwable e) {
                //ignore error
            }
        }else if(ex instanceof CodedException ce) {
            try{
                res.getWriter().write(JSONUtil.toJsonStr(ResVo.error(ce.getCode(), ce.getMessage())));
            }catch (Throwable e) {
                //ignore error
            }
        }else {
            try{
                res.getWriter().write(JSONUtil.toJsonStr(ResVo.error(ex.getMessage())));
            }catch (Throwable e) {
                //ignore error
            }
        }
        return new ModelAndView();
    }

}
