package boluo.chat.rest.message;

import boluo.chat.common.ResVo;
import boluo.chat.message.Message;
import boluo.chat.service.message.MessageService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

@Setter
@RestController
public class MessageRest {

    @Resource
    private MessageService messageService;

    /**
     * 发送消息
     * */
    @PostMapping("/Tenants/{tenantId}/Messages")
    public ResVo<?> createMessage(@PathVariable("tenantId") Long tenantId, @Valid @RequestBody Message message) {
        message.setTenantId(tenantId.toString());
        message.setTimestamp(System.currentTimeMillis());
        message = messageService.recordMessage(message);
        messageService.sendMessage(message);
        return ResVo.success(message);
    }

    /**
     * 查询消息
     * */
    @GetMapping("/Tenants/{tenantId}/Accounts/{accountId}/Messages")
    public ResVo<?> findMessages(@PathVariable("tenantId") Long tenantId, @PathVariable("accountId") Long accountId, FindMessagesReq req) {

        return ResVo.success();
    }

    /**
     * 查询群消息
     * */
    @GetMapping("/Tenants/{tenantId}/Groups/{groupId}/Messages")
    public ResVo<?> findGroupMessages(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") Long groupId, FindGroupMessagesReq req) {

        return ResVo.success();
    }

}
