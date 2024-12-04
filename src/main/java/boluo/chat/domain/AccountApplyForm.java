package boluo.chat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@TableName("account_apply_form")
public class AccountApplyForm {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @TableField("tenant_id")
    private Long tenantId;
    @TableField("account_id")
    private Long accountId;
    @TableField("apply_account_id")
    private Long applyAccountId;
    @TableField("status")
    private Integer status;
    @TableField("deleted")
    private Long deleted = 0L;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
