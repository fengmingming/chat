package boluo.chat.mapper;

import boluo.chat.domain.Tenant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface TenantMapper extends BaseMapper<Tenant> {

    default Tenant selectByPhone(String phone) {
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tenant::getPhone, phone);
        return selectOne(queryWrapper);
    }

}
