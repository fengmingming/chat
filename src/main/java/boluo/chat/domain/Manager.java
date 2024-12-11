package boluo.chat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@TableName("manager")
public class Manager {

    @TableId(value = "id", type= IdType.ASSIGN_ID)
    private Long id;
    @TableField("username")
    private String username;
    @JsonIgnore
    @TableField("password")
    private String password;
    @TableField("phone")
    private String phone;
    @TableField("state")
    private Integer state;
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;

}
