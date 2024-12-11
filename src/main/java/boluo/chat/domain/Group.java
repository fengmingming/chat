package boluo.chat.domain;

import boluo.chat.common.StringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@TableName("`group`")
public class Group {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonIgnore
    private Long id;
    @TableField(value = "group_id")
    private String groupId;
    @TableField(value = "tenant_id")
    @JsonSerialize(using = StringSerializer.class)
    private Long tenantId;
    @TableField(value = "group_name")
    private String groupName;
    @TableField(value = "state")
    private Integer state;
    @TableField(value = "deleted")
    private Long deleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

}
