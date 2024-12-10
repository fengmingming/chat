package boluo.chat.rest.account;

import boluo.chat.common.StringSerializer;
import boluo.chat.domain.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class FindFriendApplyFormsRes {

    @JsonSerialize(using = StringSerializer.class)
    private Long id;
    @JsonSerialize(using = StringSerializer.class)
    private Long tenantId;
    private Account account;
    private Account applyAccount;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
