package boluo.chat.rest.group;

import boluo.chat.common.StringSerializer;
import boluo.chat.domain.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class FindGroupApplyFormsRes {

    @JsonSerialize(using = StringSerializer.class)
    private Long id;
    private Account account;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
