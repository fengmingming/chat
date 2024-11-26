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
@TableName("relationship")
public class Relationship {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 租户
     * */
    @TableField(value = "tenant_id")
    private Long tenantId;
    /**
     * 账号id
     * */
    @TableField(value = "account_id")
    private Long accountId;
    /**
     * 好友账号id
     * */
    @TableField(value = "friend_id")
    private Long friendId;
    /**
     * 状态
     * */
    @TableField(value = "state")
    private Integer state;
    /**
     * 删除状态
     * */
    @TableField(value = "deleted")
    private Long deleted = 0L;
    /**
     * 创建时间
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 修改时间
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

}
