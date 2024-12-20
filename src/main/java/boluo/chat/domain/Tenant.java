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
@TableName("tenant")
public class Tenant {

    /**
     * 租户id
     * */
    @TableId(value = "tenant_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = StringSerializer.class)
    private Long tenantId;
    /**
     * 租户名称
     * */
    @TableField(value = "tenant_name")
    private String tenantName;
    /**
     * 电话
     * */
    @TableField(value = "phone")
    private String phone;
    /**
     * 密码
     * */
    @TableField(value = "password")
    @JsonIgnore
    private String password;
    /**
     * 租期
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "lease_deadline_date")
    private LocalDateTime leaseDeadlineDate;
    /**
     * 签名密钥，用于签名
     * */
    @TableField(value = "sign_secret")
    @JsonIgnore
    private String signSecret;
    /**
     * 超时时间
     * */
    @TableField(value = "timeout")
    private long timeout = 24;
    /**
     * 公钥，用于加解密
     * */
    @TableField(value = "public_key")
    @JsonIgnore
    public String publicKey;
    /**
     * 私钥，用于加解密
     * */
    @TableField(value = "private_key")
    @JsonIgnore
    public String privateKey;
    /**
     * 状态
     * */
    @TableField(value = "state")
    private Integer state;
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
