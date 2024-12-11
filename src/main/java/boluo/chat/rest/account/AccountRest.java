package boluo.chat.rest.account;

import boluo.chat.common.AccessValidator;
import boluo.chat.common.PageVo;
import boluo.chat.common.ResVo;
import boluo.chat.common.Session;
import boluo.chat.domain.Account;
import boluo.chat.domain.FriendApplyForm;
import boluo.chat.domain.FriendApplyFormStatusEnum;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.FriendApplyFormMapper;
import boluo.chat.service.account.AccountService;
import boluo.chat.service.account.ApplyToAddFriendCommand;
import boluo.chat.service.account.UpdateFriendApplyFormStatusCommand;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
@RestController
public class AccountRest {

    @Resource
    private AccountMapper accountMapper;
    @Resource
    private AccountService accountService;
    @Resource
    private FriendApplyFormMapper friendApplyFormMapper;
    @Resource
    private AccessValidator accessValidator;

    /**
     *  创建账户
     */
    @PostMapping("/Tenants/{tenantId}/Accounts")
    public ResVo<?> createAccount(@PathVariable("tenantId") Long tenantId, @Valid @RequestBody CreateAccountReq req) {
        Session.currentSession().verifyTenantIdAndThrowException(tenantId);
        Account account = accountMapper.selectByAccount(tenantId, req.getAccount());
        if(account != null) {
            return ResVo.error("账号已存在");
        }
        account = accountService.createAccount(tenantId, req);
        return ResVo.success(account);
    }

    /**
     * 修改账户
     * */
    @PutMapping("/Tenants/{tenantId}/Accounts/{account}")
    public ResVo<?> updateAccount(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, @Valid @RequestBody UpdateAccountReq req) {
        Session.currentSession().verifyTenantIdAndThrowException(tenantId).verifyAccountAndThrowException(account);
        accountService.updateAccount(tenantId, account, req);
        return ResVo.success();
    }

    /**
     * 查询账户
     * */
    @GetMapping("/Tenants/{tenantId}/Accounts")
    public ResVo<?> findAccounts(@PathVariable("tenantId") Long tenantId, FindAccountsReq req) {
        Session session = Session.currentSession();
        if(session.isAccount()) {
            return ResVo.error(403);
        }
        session.verifyTenantIdAndThrowException(tenantId);
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getTenantId, tenantId);
        queryWrapper.like(StrUtil.isNotBlank(req.getNickName()), Account::getNickName, req.getNickName());
        Page<Account> page = accountMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize()), queryWrapper);
        return ResVo.success(new PageVo<>(page.getTotal(), page.getRecords()));
    }

    /**
     * 查询账户
     * */
    @GetMapping("/Tenants/{tenantId}/Accounts/{account}")
    public ResVo<?> findAccounts(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account) {
        Session.currentSession().verifyTenantIdAndThrowException(tenantId).verifyAccountAndThrowException(account);
        return ResVo.success(accountMapper.selectByAccount(tenantId, account));
    }

    /**
     * 加好友
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Accounts/{account}", params = "action=addFriend")
    public ResVo<?> addFriend(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, @RequestBody AddFriendReq req) {
        Session session = Session.currentSession();
        if(!session.isTenant()) {
            return ResVo.error(403);
        }
        session.verifyTenantIdAndThrowException(tenantId).verifyAccountAndThrowException(account);
        accessValidator.verifyAccountAndThrowException(tenantId, req.getFriendAccount());

        accountService.addFriend(tenantId, account, req);
        return ResVo.success();
    }

    /**
     * 申请
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Accounts/{account}/FriendApplyForms")
    public ResVo<?> applyToAddFriend(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, @Valid @RequestBody ApplyToAddFriendReq req) {
        Session session = Session.currentSession();
        session.verifyAccountAndThrowException(account).verifyTenantIdAndThrowException(tenantId);
        accessValidator.verifyAccountAndThrowException(tenantId, req.getAccount());

        ApplyToAddFriendCommand command = new ApplyToAddFriendCommand();
        command.setAccount(req.getAccount());
        command.setApplyAccount(account);
        command.setTenantId(tenantId);
        command.setRemark(req.getRemark());
        accountService.applyToAddFriend(command);
        return ResVo.success();
    }

    /**
     * 审核好友申请
     * */
    @PutMapping(value = "/Tenants/{tenantId}/Accounts/{account}/FriendApplyForms/{friendApplyFormId}")
    public ResVo<?> updateFriendApplyFormStatus(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account,
                                                 @PathVariable("friendApplyFormId") Long friendApplyFormId, @Valid @RequestBody UpdateFriendApplyFormStatusReq req) {
        Session session = Session.currentSession();
        session.verifyTenantIdAndThrowException(tenantId).verifyAccountAndThrowException(account);

        UpdateFriendApplyFormStatusCommand command = new UpdateFriendApplyFormStatusCommand();
        command.setTenantId(tenantId);
        command.setFriendApplyFormId(friendApplyFormId);
        command.setStatus(FriendApplyFormStatusEnum.findByCode(req.getStatus()));
        command.setAccount(account);
        accountService.updateFriendApplyFormStatus(command);
        return ResVo.success();
    }

    /**
     * 查询加好友申请
     * */
    @GetMapping("/Tenants/{tenantId}/Accounts/{account}/FriendApplyForms")
    public ResVo<?> findFriendApplyForms(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, FindFriendApplyFormsReq req) {
        Session session = Session.currentSession();
        session.verifyTenantIdAndThrowException(tenantId).verifyAccountAndThrowException(account);

        Account accountEntity = accountMapper.selectByAccount(tenantId, account);
        LambdaQueryWrapper<FriendApplyForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FriendApplyForm::getTenantId, tenantId)
                .eq(req.getType() == 1, FriendApplyForm::getApplyAccountId, accountEntity.getId())
                .eq(req.getType() == 2, FriendApplyForm::getAccountId, accountEntity.getId())
                .eq(FriendApplyForm::getDeleted, 0L).orderByDesc(FriendApplyForm::getId);
        List<FriendApplyForm> forms = friendApplyFormMapper.selectList(queryWrapper);
        Collection<Long> accountIds = CollectionUtil.union(forms.stream().map(FriendApplyForm::getAccountId).toList(), forms.stream().map(FriendApplyForm::getApplyAccountId).toList());
        Map<Long, Account> accountEntities = accountMapper.selectByIds(accountIds).stream().collect(Collectors.toMap(Account::getId, Function.identity()));
        return ResVo.success(forms.stream().map(it -> {
            Account friendAccount = accountEntities.get(it.getAccountId());
            Account applyAccount = accountEntities.get(it.getApplyAccountId());
            FindFriendApplyFormsRes res = new FindFriendApplyFormsRes();
            res.setId(it.getId());
            res.setTenantId(it.getTenantId());
            res.setAccount(friendAccount);
            res.setApplyAccount(applyAccount);
            res.setStatus(it.getStatus());
            res.setCreateTime(it.getCreateTime());
            return res;
        }).toList());
    }

    /**
     * 删除好友
     * */
    @DeleteMapping(value = "/Tenants/{tenantId}/Accounts/{account}", params = "action=deleteFriend")
    public ResVo<?> deleteFriend(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, @RequestBody DeleteFriendReq req) {
        Session session = Session.currentSession();
        session.verifyTenantIdAndThrowException(tenantId).verifyAccountAndThrowException(account);
        accountService.deleteFriend(tenantId, account, req);
        return ResVo.success();
    }


}
