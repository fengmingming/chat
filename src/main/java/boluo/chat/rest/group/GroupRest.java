package boluo.chat.rest.group;

import boluo.chat.common.AccessValidator;
import boluo.chat.common.AccountSession;
import boluo.chat.common.ResVo;
import boluo.chat.common.Session;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Resource
    private AccessValidator accessValidator;

    /**
     * 创建群
     */
    @PostMapping("/Tenants/{tenantId}/Groups")
    public ResVo<?> createGroup(@PathVariable("tenantId") Long tenantId, @Valid @RequestBody CreateGroupReq req) {
        Session.currentSession().verifyTenantIdAndThrowException(tenantId);
        Group group = groupService.createGroup(tenantId, req);
        return ResVo.success(group);
    }

    /**
     * 修改群
     */
    @PutMapping("/Tenants/{tenantId}/Groups/{groupId}")
    public ResVo<?> updateGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId, @Valid @RequestBody UpdateGroupReq req) {
        Session session = Session.currentSession();
        session.verifyTenantIdAndThrowException(tenantId);
        if(session.isTenant()) {
            accessValidator.verifyGroupAndThrowException(tenantId, groupId);
        }else if(session.isAccount()) {
            accessValidator.verifyGroupMemberAndThrowException(tenantId, groupId, ((AccountSession) session).findAccount(tenantId), GroupMemberRoleEnum.admin);
        }
        groupService.updateGroup(tenantId, groupId, req);
        return ResVo.success();
    }

    /**
     * 解散
     */
    @DeleteMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=disbandGroup")
    public ResVo<?> disbandGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId) {
        Session session = Session.currentSession();
        session.verifyTenantIdAndThrowException(tenantId);
        if(session.isTenant()) {
            accessValidator.verifyGroupAndThrowException(tenantId, groupId);
        }else if(session.isAccount()) {
            accessValidator.verifyGroupMemberAndThrowException(tenantId, groupId, ((AccountSession) session).findAccount(tenantId), GroupMemberRoleEnum.admin);
        }

        groupService.disbandGroup(tenantId, groupId);
        return ResVo.success();
    }

    /**
     * 退群
     * */
    @DeleteMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=leaveGroup")
    public ResVo<?> leaveGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId, @RequestBody LeaveGroupReq req) {
        Session session = Session.currentSession();
        session.verifyTenantIdAndThrowException(tenantId);
        if(session.isTenant()) {
            accessValidator.verifyGroupAndThrowException(tenantId, groupId);
        }else if(session.isAccount()) {
            accessValidator.verifyGroupMemberAndThrowException(tenantId, groupId, ((AccountSession) session).findAccount(tenantId), GroupMemberRoleEnum.member);
        }

        groupService.leaveGroup(tenantId, groupId, req);
        return ResVo.success();
    }

    /**
     * 加群
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=joinGroup")
    public ResVo<?> joinGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId, @RequestBody JoinGroupReq req) {
        Session session = Session.currentSession();
        if(session.isAccount()){
            return ResVo.error(403);
        }
        session.verifyTenantIdAndThrowException(tenantId);
        accessValidator.verifyGroupAndThrowException(tenantId, groupId);

        groupService.joinGroup(tenantId, groupId, req);
        return ResVo.success();
    }

    /**
     * 申请
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=applyToJoinGroup")
    public ResVo<?> applyToJoinGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId, @RequestBody ApplyToJoinGroupReq req) {
        Session.currentSession().verifyTenantIdAndThrowException(tenantId);
        accessValidator.verifyGroupAndThrowException(tenantId, groupId);

        req.setTenantId(tenantId);
        req.setGroupId(groupId);
        groupService.applyToJoinGroup(req);
        return ResVo.success();
    }

    /**
     * 查询申请
     * */
    @GetMapping("/Tenants/{tenantId}/Groups/{groupId}/GroupApplyForms")
    public ResVo<?> findGroupApplyForms(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId, @Valid FindGroupApplyFormsReq req) {
        Session session = Session.currentSession();
        session.verifyTenantIdAndThrowException(tenantId);
        accessValidator.verifyGroupAndThrowException(tenantId, groupId);
        if(session.isAccount() && !((AccountSession) session).findAccount(tenantId).equals(req.getAccount())) {
            return ResVo.error(403);
        }
        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        Account account = accountMapper.selectByAccount(tenantId, req.getAccount());
        GroupMember gm = groupMemberMapper.selectByAccountId(tenantId, group.getId(), account.getId());
        List<GroupApplyForm> forms = null;
        if(gm != null && GroupMemberRoleEnum.valueOf(gm.getRole()) == GroupMemberRoleEnum.admin) {
            //群主
            LambdaQueryWrapper<GroupApplyForm> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GroupApplyForm::getTenantId, tenantId).eq(GroupApplyForm::getGroupId, group.getId()).eq(GroupApplyForm::getDeleted, 0L);
            queryWrapper.eq(req.getStatus() != null, GroupApplyForm::getStatus, req.getStatus());
            queryWrapper.orderByDesc(GroupApplyForm::getId);
            forms = groupApplyFormMapper.selectList(queryWrapper);
        }else {
            //个人
            LambdaQueryWrapper<GroupApplyForm> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GroupApplyForm::getTenantId, tenantId).eq(GroupApplyForm::getGroupId, group.getId()).eq(GroupApplyForm::getDeleted, 0L);
            queryWrapper.eq(GroupApplyForm::getAccountId, account.getId());
            queryWrapper.eq(req.getStatus() != null, GroupApplyForm::getStatus, req.getStatus());
            queryWrapper.orderByDesc(GroupApplyForm::getId);
            forms = groupApplyFormMapper.selectList(queryWrapper);
        }
        Map<Long, Account> accountMap = new HashMap<>();
        List<Long> accountIds = forms.stream().map(GroupApplyForm::getAccountId).toList();
        if(!accountIds.isEmpty()) {
            accountMap.putAll(accountMapper.selectByIds(accountIds).stream().collect(Collectors.toMap(Account::getId, Function.identity())));
        }
        return ResVo.success(forms.stream().map(form -> {
            FindGroupApplyFormsRes res = new FindGroupApplyFormsRes();
            res.setId(form.getId());
            res.setAccount(accountMap.get(form.getAccountId()));
            res.setStatus(form.getStatus());
            res.setCreateTime(form.getCreateTime());
            return res;
        }).toList());
    }

    /**
     * 审核申请
     * */
    @PutMapping("/Tenants/{tenantId}/Groups/{groupId}/GroupApplyForms/{groupApplyFormId}")
    public ResVo<?> updateGroupApplyFormStatus(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId,
                                               @PathVariable("groupApplyFormId") Long groupApplyFormId, @Valid @RequestBody UpdateGroupApplyFormStatusReq req) {
        Session session = Session.currentSession();
        session.verifyTenantIdAndThrowException(tenantId);
        session.verifyAccountAndThrowException(req.getAccount());
        if(session.isTenant()) {
            accessValidator.verifyGroupAndThrowException(tenantId, groupId);
        }else if(session.isAccount()) {
            accessValidator.verifyGroupMemberAndThrowException(tenantId, groupId, ((AccountSession) session).findAccount(tenantId), GroupMemberRoleEnum.admin);
        }

        UpdateGroupApplyFormStatusCommand command = new UpdateGroupApplyFormStatusCommand();
        command.setGroupApplyFormId(groupApplyFormId);
        command.setTenantId(tenantId);
        command.setGroupId(groupId);
        command.setAccount(req.getAccount());
        command.setStatus(GroupApplyFormStatusEnum.findByCode(req.getStatus()));
        groupService.updateGroupApplyFormStatus(command);
        return ResVo.success();
    }

    @GetMapping("/Tenants/{tenantId}/Groups/{groupId}")
    public ResVo<?> findGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId) {
        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        return ResVo.success(group);
    }

    /**
     * 群成员
     * */
    @GetMapping("/Tenants/{tenantId}/Groups/{groupId}/Members")
    public ResVo<?> findGroupMembers(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") String groupId) {
        Session session = Session.currentSession();
        session.verifyTenantIdAndThrowException(tenantId);
        if(session.isTenant()) {
            accessValidator.verifyGroupAndThrowException(tenantId, groupId);
        }else if(session.isAccount()) {
            accessValidator.verifyGroupMemberAndThrowException(tenantId, groupId, ((AccountSession) session).findAccount(tenantId));
        }

        Group group = groupMapper.selectByGroupId(tenantId, groupId);
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getTenantId, tenantId);
        queryWrapper.eq(GroupMember::getGroupId, group.getId());
        queryWrapper.eq(GroupMember::getDeleted, 0L);
        List<GroupMember> gms = groupMemberMapper.selectList(queryWrapper);
        if(!gms.isEmpty()) {
            List<Account> accountEntities = accountMapper.selectByIds(gms.stream().map(GroupMember::getAccountId).toList());
            return ResVo.success(accountEntities);
        }
        return ResVo.success();
    }

    /**
     * 群成员
     * */
    @GetMapping("/Tenants/Groups/Members")
    public ResVo<?> listGroupMembers(@RequestParam("tenantId") Long tenantId, @RequestParam("groupId") String groupId) {
        if(!Session.currentSession().isManager()) {
            return ResVo.error(403);
        }
        return findGroupMembers(tenantId, groupId);
    }

    /**
     * 我的群
     * */
    @GetMapping("/Tenants/{tenantId}/Accounts/{account}/Groups")
    public ResVo<?> findGroups(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, FindGroupsReq req) {
        Session session = Session.currentSession();
        session.verifyTenantIdAndThrowException(tenantId).verifyAccountAndThrowException(account);
        accessValidator.verifyAccountAndThrowException(tenantId, account);

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
