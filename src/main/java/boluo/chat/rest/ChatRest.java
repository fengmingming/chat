package boluo.chat.rest;

import boluo.chat.common.ResVo;
import boluo.chat.service.ChatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatRest {

    @Resource
    private ChatService chatService;

    @PostMapping("/Tenant")
    public ResVo<?> createTenant(@Valid @RequestBody CreateTenantReq req) {
        return ResVo.success();
    }


}
