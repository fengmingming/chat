package boluo.chat.rsocket;

import boluo.chat.config.ChatProperties;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@ConditionalOnProperty(value = "boluo.chat.im-address", matchIfMissing = false)
public class DirectAddressFactory implements AddressFactory, InitializingBean {

    @Setter
    @Resource
    private ChatProperties chatProperties;
    private final List<Address> addresses = new ArrayList<>();
    private final AtomicLong count = new AtomicLong(0L);

    @Override
    public Address findAddress() {
        long count = this.count.getAndIncrement();
        return addresses.get((int) (count % addresses.size()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String imAddress = chatProperties.getImAddress();
        if(StrUtil.isNotBlank(imAddress)) {
            String[] ip_ports = imAddress.split(",");
            for(String s : ip_ports) {
                String[] ip_port = s.split(":");
                Address address = new Address(ip_port[0].trim(), Integer.parseInt(ip_port[1].trim()));
                this.addresses.add(address);
            }
        }
    }

}
