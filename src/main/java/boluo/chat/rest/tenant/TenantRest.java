package boluo.chat.rest.tenant;

import boluo.chat.common.ResVo;
import boluo.chat.service.tenant.TenantService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class TenantRest {

    @Resource
    private TenantService tenantService;

    /**
     * 创建租户
     * */
    @PostMapping("/Tenants")
    public ResVo<?> createTenant(@Valid @RequestBody CreateTenantReq req) {

        tenantService.createTenant(req);
        return ResVo.success();
    }

    /**
     * 编辑租户
     * */
    @PutMapping("/Tenants/{tenantId}")
    public ResVo<?> updateTenant(@PathVariable Long tenantId, @Valid @RequestBody UpdateTenantReq req) {

        return ResVo.success();
    }

    /**
     * 查询租户
     * */
    @GetMapping("/Tenants")
    public ResVo<?> findTenants(FindTenantsReq req) {

        return ResVo.success();
    }

    /**
     * 查询租户
     * */
    @GetMapping("/Tenants/{tenantId}")
    public ResVo<?> findTenant(@PathVariable("tenantId") Long tenantId) {

        return ResVo.success();
    }

}
