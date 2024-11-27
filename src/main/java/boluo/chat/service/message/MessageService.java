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
import org.springframework.stereotype.Service;

@Service
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
    public Message recordAndSendMessage(Message message) {
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
        messageMapper.insert(me);
        message.setMsgId(me.getMsgId().toString());
        //保存
        Address address = addressFactory.findAddress();
        MessageRequester requester = rsocketFactory.getMessageRequester(address);
        requester.sendMessage(message).subscribe();
        return message;
    }

}
