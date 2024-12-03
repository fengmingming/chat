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
@TableName("group_apply_form")
public class GroupApplyForm {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @TableField("tenant_id")
    private Long tenantId;
    @TableField("group_id")
    private Long groupId;
    @TableField("account_id")
    private Long accountId;
    //10 已申请 20 已同意 30 拒绝
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
