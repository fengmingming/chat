package boluo.chat.rest.account;

import boluo.chat.common.Paging;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindAccountsReq extends Paging {

    private String nickName;

}
