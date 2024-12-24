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
@TableName("account")
public class Account {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonIgnore
    private Long id;
    @TableField(value = "tenant_id")
    @JsonSerialize(using = StringSerializer.class)
    private Long tenantId;
    @TableField(value = "account")
    private String account;
    @JsonIgnore
    @TableField(value = "password")
    private String password;
    @TableField(value = "nick_name")
    private String nickName;
    @TableField(value = "first_letter")
    private Character firstLetter;
    @TableField(value = "profile_picture")
    private String profilePicture;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

}
