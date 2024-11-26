package boluo.chat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("message")
public class MessageEntity {

    @TableId(value = "msg_id", type = IdType.ASSIGN_ID)
    private Long msgId;
    @TableField(value = "tenant_id")
    private Long tenantId;
    @TableField(value = "from")
    private String from;
    @TableField(value = "to")
    private String to;
    @TableField(value = "msg_type")
    private String msgType;
    @TableField(value = "message")
    private String message;

}
