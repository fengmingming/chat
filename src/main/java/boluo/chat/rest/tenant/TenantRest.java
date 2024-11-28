package boluo.chat.rest.tenant;

import boluo.chat.common.PageVo;
import boluo.chat.common.ResVo;
import boluo.chat.domain.Tenant;
import boluo.chat.mapper.TenantMapper;
import boluo.chat.service.tenant.TenantService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class TenantRest {

    @Resource
    private TenantService tenantService;
    @Resource
    private TenantMapper tenantMapper;

    /**
     * 创建租户
     * */
    @PostMapping("/Tenants")
    public ResVo<?> createTenant(@Valid @RequestBody CreateTenantReq req) {
        Tenant tenant = tenantMapper.selectByPhone(req.getPhone());
        if(tenant != null) {
            return ResVo.error("手机号已被注册");
        }
        tenant = tenantService.createTenant(req);
        return ResVo.success(tenant);
    }

    /**
     * 编辑租户
     * */
    @PutMapping("/Tenants/{tenantId}")
    public ResVo<?> updateTenant(@PathVariable Long tenantId, @Valid @RequestBody UpdateTenantReq req) {
        tenantService.updateTenant(tenantId, req);
        return ResVo.success();
    }

    /**
     * 查询租户
     * */
    @GetMapping("/Tenants")
    public ResVo<?> findTenants(FindTenantsReq req) {
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(req.getPhone()), Tenant::getPhone, req.getPhone());
        queryWrapper.like(StrUtil.isNotBlank(req.getTenantName()), Tenant::getTenantName, req.getTenantName());
        queryWrapper.eq(req.getState() != null, Tenant::getState, req.getState());
        Page<Tenant> page = tenantMapper.selectPage(new Page<Tenant>(req.getPageNum(), req.getPageSize()), queryWrapper);
        return ResVo.success(new PageVo<Tenant>(page.getTotal(), page.getRecords()));
    }

    /**
     * 查询租户
     * */
    @GetMapping("/Tenants/{tenantId}")
    public ResVo<?> findTenant(@PathVariable("tenantId") Long tenantId) {
        return ResVo.success(tenantMapper.selectById(tenantId));
    }

}
