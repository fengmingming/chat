package boluo.chat.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AccountSession extends Session{

    private List<SimpleAccount> accounts;

    public AccountSession() {
        super(SessionRoleEnum.Account);
    }

}
