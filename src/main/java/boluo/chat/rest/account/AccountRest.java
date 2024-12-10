package boluo.chat.rest.account;

import boluo.chat.common.PageVo;
import boluo.chat.common.ResVo;
import boluo.chat.domain.Account;
import boluo.chat.domain.FriendApplyForm;
import boluo.chat.domain.FriendApplyFormStatusEnum;
import boluo.chat.mapper.AccountMapper;
import boluo.chat.mapper.FriendApplyFormMapper;
import boluo.chat.service.account.AccountService;
import boluo.chat.service.account.ApplyToAddFriendCommand;
import boluo.chat.service.account.UpdateFriendApplyFormStatusCommand;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

@Setter
@RestController
public class AccountRest {

    @Resource
    private AccountMapper accountMapper;
    @Resource
    private AccountService accountService;
    @Resource
    private FriendApplyFormMapper friendApplyFormMapper;

    /**
     *  账户
     */
    @PostMapping("/Tenants/{tenantId}/Accounts")
    public ResVo<?> createAccount(@PathVariable("tenantId") Long tenantId, @Valid @RequestBody CreateAccountReq req) {
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
        accountService.updateAccount(tenantId, account, req);
        return ResVo.success();
    }

    /**
     * 查询账户
     * */
    @GetMapping("/Tenants/{tenantId}/Accounts")
    public ResVo<?> findAccounts(@PathVariable("tenantId") Long tenantId, FindAccountsReq req) {
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
        return ResVo.success(accountMapper.selectByAccount(tenantId, account));
    }

    /**
     * 加好友
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Accounts/{account}", params = "action=addFriend")
    public ResVo<?> addFriend(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, @RequestBody AddFriendReq req) {
        accountService.addFriend(tenantId, account, req);
        return ResVo.success();
    }

    /**
     * 申请
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Accounts/{account}/FriendApplyForms")
    public ResVo<?> applyToAddFriend(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, @Valid @RequestBody ApplyToAddFriendReq req) {
        ApplyToAddFriendCommand command = new ApplyToAddFriendCommand();
        command.setAccount(req.getAccount());
        command.setApplyAccount(account);
        command.setTenantId(tenantId);
        command.setRemark(req.getRemark());
        accountService.applyToAddFriend(command);
        return ResVo.success();
    }

    @PutMapping(value = "/Tenants/{tenantId}/Accounts/{account}/FriendApplyForms/{friendApplyFormId}")
    public ResVo<?> updateFriendApplyFormStatus(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account,
                                                 @PathVariable("friendApplyFormId") Long friendApplyFormId, @Valid @RequestBody UpdateFriendApplyFormStatusReq req) {
        UpdateFriendApplyFormStatusCommand command = new UpdateFriendApplyFormStatusCommand();
        command.setTenantId(tenantId);
        command.setFriendApplyFormId(friendApplyFormId);
        command.setStatus(FriendApplyFormStatusEnum.findByCode(req.getStatus()));
        command.setAccount(account);
        accountService.updateFriendApplyFormStatus(command);
        return ResVo.success();
    }

    @GetMapping("/Tenants/{tenantId}/Accounts/{account}/FriendApplyForms")
    public ResVo<?> findFriendApplyForms(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, FindFriendApplyFormsReq req) {
        Account accountEntity = accountMapper.selectByAccount(tenantId, account);
        LambdaQueryWrapper<FriendApplyForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FriendApplyForm::getTenantId, tenantId)
                .eq(req.getType() == 1, FriendApplyForm::getApplyAccountId, accountEntity.getId())
                .eq(req.getType() == 2, FriendApplyForm::getAccountId, accountEntity.getId())
                .eq(FriendApplyForm::getDeleted, 0L).orderByDesc(FriendApplyForm::getId);
        return ResVo.success();
    }

    /**
     * 删除好友
     * */
    @DeleteMapping(value = "/Tenants/{tenantId}/Accounts/{account}", params = "action=deleteFriend")
    public ResVo<?> deleteFriend(@PathVariable("tenantId") Long tenantId, @PathVariable("account") String account, @RequestBody DeleteFriendReq req) {
        accountService.deleteFriend(tenantId, account, req);
        return ResVo.success();
    }


}
