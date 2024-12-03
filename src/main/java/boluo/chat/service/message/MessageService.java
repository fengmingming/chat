package boluo.chat.service.message;

import boluo.chat.domain.MessageEntity;
import boluo.chat.mapper.MessageMapper;
import boluo.chat.message.Message;
import boluo.chat.message.MessageRequester;
import boluo.chat.rsocket.Address;
import boluo.chat.rsocket.AddressFactory;
import boluo.chat.rsocket.RSocketFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class MessageService {

    @Resource
    private AddressFactory addressFactory;
    @Resource
    private RSocketFactory rsocketFactory;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 记录并发送消息
     * */
    @Transactional
    public Message recordMessage(@Valid Message message) {
        //保存
        MessageEntity me = new MessageEntity();
        me.setTenantId(Long.parseLong(message.getTenantId()));
        me.setFrom(message.getFrom());
        me.setTo(message.getTo());
        me.setMsgType(message.findMsgType());
        try {
            me.setMessage(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if(message.getTimestamp() == null) {
            me.setTimestamp(System.currentTimeMillis());
        }else {
            me.setTimestamp(message.getTimestamp());
        }
        messageMapper.insert(me);
        message.setMsgId(me.getMsgId().toString());
        return message;
    }

    public void sendMessage(Message message) {
        //发送
        Address address = addressFactory.findAddress();
        MessageRequester requester = rsocketFactory.getMessageRequester(address);
        requester.sendMessage(message).subscribe();
    }

    @Transactional
    public void recordAndSendMessage(Message message) {
        recordMessage(message);
        sendMessage(message);
    }

}
