package boluo.chat.message;

import org.springframework.messaging.rsocket.service.RSocketExchange;
import reactor.core.publisher.Mono;

public interface MessageRequester {

    @RSocketExchange("chat.message.send")
    public Mono<Void> sendMessage(Message message);

}
