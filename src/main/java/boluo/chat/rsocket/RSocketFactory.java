package boluo.chat.rsocket;

import boluo.chat.message.MessageRequester;
import cn.hutool.cache.impl.TimedCache;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.service.RSocketServiceProxyFactory;
import org.springframework.stereotype.Component;

@Component
public class RSocketFactory {

    private final TimedCache<Address, MessageRequester> cache = new TimedCache<>(1000 * 60 * 10);
    @Setter
    @Resource
    private RSocketRequester.Builder requestBuilder;

    public MessageRequester getMessageRequester(Address address) {
        MessageRequester router = cache.get(address);
        if(router == null) {
            router = createMessageRequester(address);
        }
        return router;
    }

    protected synchronized MessageRequester createMessageRequester(Address address) {
        RSocketRequester requester = requestBuilder.tcp(address.getIp(), address.getPort());
        MessageRequester remoteRouter = RSocketServiceProxyFactory.builder(requester).build().createClient(MessageRequester.class);
        cache.put(address, remoteRouter);
        return remoteRouter;
    }

}
