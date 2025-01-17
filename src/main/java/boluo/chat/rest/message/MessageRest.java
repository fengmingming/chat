package boluo.chat.rest.message;

import boluo.chat.common.AccessValidator;
import boluo.chat.common.AccountSession;
import boluo.chat.common.ResVo;
import boluo.chat.common.Session;
import boluo.chat.domain.MessageEntity;
import boluo.chat.mapper.MessageMapper;
import boluo.chat.message.Message;
import boluo.chat.service.message.MessageService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

@Setter
@RestController
public class MessageRest {

    @Resource
    private MessageService messageService;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private AccessValidator accessValidator;

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
    @GetMapping("/Tenants/{tenantId}/Accounts/{account}/Messages")
    public ResVo<?> findMessages(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, FindMessagesReq req) {
        Session.currentSession().verifyTenantIdAndThrowException(tenantId).verifyAccountAndThrowException(account);
        accessValidator.verifyFriend(tenantId, account, req.getAccount());
        if(req.getStartTime() == null) {
            req.setStartTime(LocalDateTime.now().plusDays(-7));
        }
        if(req.getEndTime() == null) {
            req.setEndTime(LocalDateTime.now());
        }
        //发我的
        LambdaQueryWrapper<MessageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessageEntity::getTenantId, tenantId);
        queryWrapper.eq(MessageEntity::getTo, account);
        queryWrapper.eq(MessageEntity::getFrom, req.getAccount());
        queryWrapper.gt(req.getMaxMsgId() != null, MessageEntity::getMsgId, req.getMaxMsgId());
        if(req.getStartTime() != null) {
            queryWrapper.ge(MessageEntity::getTimestamp, req.getStartTime().toEpochSecond(ZoneOffset.ofHours(8)));
        }
        if(req.getEndTime() != null) {
            queryWrapper.lt(MessageEntity::getTimestamp, req.getEndTime().toEpochSecond(ZoneOffset.ofHours(8)));
        }
        queryWrapper.orderByAsc(MessageEntity::getMsgId);
        queryWrapper.last("LIMIT 1000");
        List<Message> toMessages = messageMapper.selectList(queryWrapper).stream().map(it -> {
            try {
                return objectMapper.readValue(it.getMessage(), Message.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
        //我发的
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessageEntity::getTenantId, tenantId);
        queryWrapper.eq(MessageEntity::getFrom, account);
        queryWrapper.eq(MessageEntity::getTo, req.getAccount());
        queryWrapper.gt(req.getMaxMsgId() != null, MessageEntity::getMsgId, req.getMaxMsgId());
        if(req.getStartTime() != null) {
            queryWrapper.ge(MessageEntity::getTimestamp, req.getStartTime().toEpochSecond(ZoneOffset.ofHours(8)));
        }
        if(req.getEndTime() != null) {
            queryWrapper.lt(MessageEntity::getTimestamp, req.getEndTime().toEpochSecond(ZoneOffset.ofHours(8)));
        }
        queryWrapper.orderByAsc(MessageEntity::getMsgId);
        queryWrapper.last("LIMIT 1000");
        List<Message> fromMessages = messageMapper.selectList(queryWrapper).stream().map(it -> {
            try {
                return objectMapper.readValue(it.getMessage(), Message.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
        return ResVo.success(CollectionUtil.unionAll(toMessages, fromMessages).stream().sorted(Comparator.comparing(Message::getMsgId)).toList());
    }

    /**
     * 查询群消息
     * */
    @GetMapping("/Tenants/{tenantId}/Groups/{groupId}/Messages")
    public ResVo<?> findGroupMessages(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId, FindGroupMessagesReq req) {
        Session session = Session.currentSession();
        session.verifyTenantIdAndThrowException(tenantId);
        if(session.isTenant()) {
            accessValidator.verifyGroupAndThrowException(tenantId, groupId);
        }else if(session.isAccount()) {
            accessValidator.verifyGroupMemberAndThrowException(tenantId, groupId, ((AccountSession) session).findAccount(tenantId));
        }
        if(req.getStartTime() == null) {
            req.setStartTime(LocalDateTime.now().plusDays(-7));
        }
        if(req.getEndTime() == null) {
            req.setEndTime(LocalDateTime.now());
        }
        LambdaQueryWrapper<MessageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessageEntity::getTenantId, tenantId);
        queryWrapper.eq(MessageEntity::getTo, "GROUP:" + groupId);
        queryWrapper.gt(req.getMaxMsgId() != null, MessageEntity::getMsgId, req.getMaxMsgId());
        if(req.getStartTime() != null) {
            queryWrapper.ge(MessageEntity::getTimestamp, req.getStartTime().toEpochSecond(ZoneOffset.ofHours(8)));
        }
        if(req.getEndTime() != null) {
            queryWrapper.lt(MessageEntity::getTimestamp, req.getEndTime().toEpochSecond(ZoneOffset.ofHours(8)));
        }
        queryWrapper.orderByAsc(MessageEntity::getMsgId);
        queryWrapper.last("LIMIT 1000");
        List<Message> messages = messageMapper.selectList(queryWrapper).stream().map(it -> {
            try {
                return objectMapper.readValue(it.getMessage(), Message.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
        return ResVo.success(messages);
    }

}
