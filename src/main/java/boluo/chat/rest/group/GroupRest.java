package boluo.chat.rest.group;

import boluo.chat.common.ResVo;
import boluo.chat.domain.Account;
import boluo.chat.domain.Group;
import boluo.chat.domain.GroupMember;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.GroupMapper;
import boluo.chat.mapper.GroupMemberMapper;
import boluo.chat.service.group.GroupService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Setter
@RestController
public class GroupRest {

    @Resource
    private GroupService groupService;
    @Resource
    private GroupMemberMapper groupMemberMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private GroupMapper groupMapper;

    /**
     * 创建群
     */
    @PostMapping("/Tenants/{tenantId}/Groups")
    public ResVo<?> createGroup(@PathVariable("tenantId") Long tenantId, @Valid @RequestBody CreateGroupReq req) {
        Group group = groupService.createGroup(tenantId, req);
        return ResVo.success(group);
    }

    /**
     * 修改群
     */
    @PutMapping("/Tenants/{tenantId}/Groups/{groupId}")
    public ResVo<?> updateGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId, @Valid @RequestBody UpdateGroupReq req) {
        groupService.updateGroup(tenantId, groupId, req);
        return ResVo.success();
    }

    /**
     * 解散
     */
    @DeleteMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=disbandGroup")
    public ResVo<?> disbandGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId) {
        groupService.disbandGroup(tenantId, groupId);
        return ResVo.success();
    }

    /**
     * 退群
     * */
    @DeleteMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=leaveGroup")
    public ResVo<?> leaveGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId, @RequestBody LeaveGroupReq req) {
        groupService.leaveGroup(tenantId, groupId, req);
        return ResVo.success();
    }

    /**
     * 加群
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=joinGroup")
    public ResVo<?> joinGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId, @RequestBody JoinGroupReq req) {
        groupService.joinGroup(tenantId, groupId, req);
        return ResVo.success();
    }

    /**
     * 申请
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=applyToJoinGroup")
    public ResVo<?> applyToJoinGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId, @RequestBody ApplyToJoinGroupReq req) {

        return ResVo.success();
    }

    /**
     * 群成员
     * */
    @GetMapping("/Tenants/{tenantId}/Groups/{groupId}/Members")
    public ResVo<?> findGroupMembers(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId) {
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getTenantId, tenantId);
        queryWrapper.eq(GroupMember::getGroupId, groupId);
        queryWrapper.eq(GroupMember::getDeleted, 0L);
        List<GroupMember> gms = groupMemberMapper.selectList(queryWrapper);
        if(!gms.isEmpty()) {
            List<Account> accountEntities = accountMapper.selectByIds(gms.stream().map(GroupMember::getAccountId).toList());
            return ResVo.success(accountEntities);
        }
        return ResVo.success();
    }

    /**
     * 我的群
     * */
    @GetMapping("/Tenants/{tenantId}/Accounts/{account}/Groups")
    public ResVo<?> findGroups(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, FindGroupsReq req) {
        Account accountEntity = accountMapper.selectByAccount(tenantId, account);
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getTenantId, tenantId);
        queryWrapper.eq(GroupMember::getAccountId, accountEntity.getId());
        queryWrapper.eq(GroupMember::getDeleted, 0L);
        List<Long> groupIds = groupMemberMapper.selectList(queryWrapper).stream().map(GroupMember::getGroupId).distinct().toList();
        if(!groupIds.isEmpty()) {
            LambdaQueryWrapper<Group> groupQueryWrapper = new LambdaQueryWrapper<>();
            groupQueryWrapper.eq(Group::getTenantId, tenantId);
            groupQueryWrapper.in(Group::getId, groupIds);
            groupQueryWrapper.eq(Group::getDeleted, 0L);
            return ResVo.success(groupMapper.selectList(groupQueryWrapper));
        }
        return ResVo.success();
    }


}
