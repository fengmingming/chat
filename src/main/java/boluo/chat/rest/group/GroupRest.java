package boluo.chat.rest.group;

import boluo.chat.common.ResVo;
import boluo.chat.domain.*;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.GroupApplyFormMapper;
import boluo.chat.mapper.GroupMapper;
import boluo.chat.mapper.GroupMemberMapper;
import boluo.chat.service.group.GroupService;
import boluo.chat.service.group.UpdateGroupApplyFormStatusCommand;
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
    @Resource
    private GroupApplyFormMapper groupApplyFormMapper;

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
        groupService.applyToJoinGroup(req);
        return ResVo.success();
    }

    /**
     * 查询申请
     * */
    @GetMapping("/Tenants/{tenantId}/Groups/{groupId}/GroupApplyForms")
    public ResVo<?> findGroupApplyForms(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId, @Valid FindGroupApplyFormsReq req) {
        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        Account account = accountMapper.selectByAccount(tenantId, req.getAccount());
        GroupMember gm = groupMemberMapper.selectByAccountId(tenantId, group.getId(), account.getId());
        if(gm != null && GroupMemberRoleEnum.valueOf(gm.getRole()) == GroupMemberRoleEnum.admin) {
            //群主
            LambdaQueryWrapper<GroupApplyForm> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GroupApplyForm::getTenantId, tenantId).eq(GroupApplyForm::getGroupId, group.getId()).eq(GroupApplyForm::getDeleted, 0L);
            queryWrapper.eq(req.getStatus() != null, GroupApplyForm::getStatus, req.getStatus());
            queryWrapper.orderByDesc(GroupApplyForm::getId);
            return ResVo.success(groupApplyFormMapper.selectList(queryWrapper));
        }else {
            //个人
            LambdaQueryWrapper<GroupApplyForm> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GroupApplyForm::getTenantId, tenantId).eq(GroupApplyForm::getGroupId, group.getId()).eq(GroupApplyForm::getDeleted, 0L);
            queryWrapper.eq(GroupApplyForm::getAccountId, account.getId());
            queryWrapper.eq(req.getStatus() != null, GroupApplyForm::getStatus, req.getStatus());
            queryWrapper.orderByDesc(GroupApplyForm::getId);
            return ResVo.success(groupApplyFormMapper.selectList(queryWrapper));
        }
    }

    @PutMapping("/Tenants/{tenantId}/Groups/{groupId}/GroupApplyForms/{groupApplyFormId}")
    public ResVo<?> updateGroupApplyFormStatus(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId,
                                               @PathVariable("groupApplyFormId") Long groupApplyFormId, @Valid @RequestBody UpdateGroupApplyFormStatusReq req) {
        UpdateGroupApplyFormStatusCommand command = new UpdateGroupApplyFormStatusCommand();
        command.setGroupApplyFormId(groupApplyFormId);
        command.setTenantId(tenantId);
        command.setGroupId(groupId);
        command.setAccount(req.getAccount());
        command.setStatus(GroupApplyFormStatusEnum.findByCode(req.getStatus()));
        groupService.updateGroupApplyFormStatus(command);
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
