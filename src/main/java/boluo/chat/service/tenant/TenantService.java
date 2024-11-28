package boluo.chat.service.tenant;

import boluo.chat.domain.Tenant;
import boluo.chat.domain.TenantStateEnum;
import boluo.chat.mapper.TenantMapper;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TenantService {

    @Resource
    private TenantMapper tenantMapper;

    @Transactional
    public Tenant createTenant(@Valid CreateTenantCommand command) {
        Tenant tenant = new Tenant();
        tenant.setTenantName(command.getTenantName());
        tenant.setPhone(command.getPhone());
        if(StrUtil.isNotBlank(command.getPassword())) {
            tenant.setPassword(BCrypt.hashpw(command.getPassword(), BCrypt.gensalt()));
        }
        tenant.setState(TenantStateEnum.INIT.getCode());
        tenant.setCreateTime(LocalDateTime.now());
        tenant.setUpdateTime(LocalDateTime.now());
        tenantMapper.insert(tenant);
        return tenant;
    }

    @Transactional
    public void updateTenant(Long tenantId, @Valid UpdateTenantCommand command) {
        Tenant tenant = tenantMapper.selectById(tenantId);
        if(command.getTenantName() != null) {
            tenant.setTenantName(command.getTenantName());
        }
        if(command.getPhone() != null) {
            tenant.setPhone(command.getPhone());
        }
        if(command.getPassword() != null) {
            tenant.setPassword(BCrypt.hashpw(command.getPassword(), BCrypt.gensalt()));
        }
        tenant.setUpdateTime(LocalDateTime.now());
        tenantMapper.updateById(tenant);
    }

}
