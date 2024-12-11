package boluo.chat.mapper;

import boluo.chat.domain.Manager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ManagerMapper extends BaseMapper<Manager> {

    default Manager selectByUsername(String username) {
        LambdaQueryWrapper<Manager> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Manager::getUsername, username);
        return selectOne(queryWrapper);
    }

}
