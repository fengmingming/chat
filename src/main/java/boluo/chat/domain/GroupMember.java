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
@TableName("group_member")
public class GroupMember {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @TableField(value = "tenant_id")
    private Long tenantId;
    @TableField(value = "group_id")
    private Long groupId;
    @TableField(value = "account_id")
    private Long accountId;
    @TableField(value = "role")
    private String role;
    @TableField(value = "deleted")
    private Long deleted = 0L;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

}
