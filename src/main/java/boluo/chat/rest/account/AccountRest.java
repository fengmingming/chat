package boluo.chat.rest.account;

import boluo.chat.common.ResVo;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountRest {

    /**
     *  账户
     */
    @PostMapping("/Tenants/{tenantId}/Accounts")
    public ResVo<?> createAccount(@PathVariable("tenantId") Long tenantId, @RequestBody CreateAccountReq req) {

        return ResVo.success();
    }

    /**
     * 修改账户
     * */
    @PutMapping("/Tenants/{tenantId}/Accounts/{accountId}")
    public ResVo<?> updateAccount(@PathVariable("tenantId") Long tenantId, @PathVariable("accountId") Long accountId, @RequestBody UpdateAccountReq req) {

        return ResVo.success();
    }

    /**
     * 查询账户
     * */
    @GetMapping("/Tenants/{tenantId}/Accounts")
    public ResVo<?> findAccounts(@PathVariable("tenantId") Long tenantId, FindAccountsReq req) {

        return ResVo.success();
    }

    /**
     * 查询账户
     * */
    @GetMapping("/Tenants/{tenantId}/Accounts/{accountId}")
    public ResVo<?> findAccounts(@PathVariable("tenantId") Long tenantId, @PathVariable("accountId") Long accountId) {

        return ResVo.success();
    }

    /**
     * 加好友
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Accounts/{accountId}", params = "action=addFriend")
    public ResVo<?> addFriend(@PathVariable("tenantId") Long tenantId, @PathVariable("accountId") Long accountId, @RequestBody AddFriendReq req) {

        return ResVo.success();
    }

    /**
     * 邀请
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Accounts/{accountId}", params = "action=applyToAddFriend")
    public ResVo<?> applyToAddFriend(@PathVariable("tenantId") Long tenantId, @PathVariable("accountId") Long accountId, @RequestBody ApplyToAddFriendReq req) {

        return ResVo.success();
    }

    /**
     * 删除好友
     * */
    @DeleteMapping(value = "/Tenants/{tenantId}/Accounts/{accountId}", params = "action=deleteFriend")
    public ResVo<?> deleteFriend(@PathVariable("tenantId") Long tenantId, @PathVariable("accountId") Long accountId, @RequestBody DeleteFriendReq req) {

        return ResVo.success();
    }


}
