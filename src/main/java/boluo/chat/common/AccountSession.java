package boluo.chat.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
public class AccountSession extends Session {

    private List<SimpleAccount> accounts;

    @Override
    public boolean verifyTenantId(Long tenantId) {
        return accounts.stream().anyMatch(it -> it.getTenantId().equals(tenantId));
    }

    @Override
    public boolean verifyAccount(String account) {
        return accounts.stream().anyMatch(it -> it.getAccount().equals(account));
    }

    public String findAccount(Long tenantId) {
        Optional<SimpleAccount> opt = accounts.stream().filter(it -> it.getTenantId().equals(tenantId)).findAny();
        if(opt.isPresent()) {
            return opt.get().getAccount();
        }
        throw new CodedException(403, "Forbidden");
    }

}
