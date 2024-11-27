package boluo.chat.rest.group;

import boluo.chat.common.ResVo;
import org.springframework.web.bind.annotation.*;

@RestController
public class GroupRest {

    /**
     * 创建群
     */
    @PostMapping("/Tenants/{tenantId}/Groups")
    public ResVo<?> createGroup(@RequestBody CreateGroupReq req) {
        return ResVo.success();
    }

    /**
     * 修改群
     */
    @PutMapping("/Tenants/{tenantId}/Groups/{groupId}")
    public ResVo<?> updateGroup(@RequestBody UpdateGroupReq req) {
        return ResVo.success();
    }

    /**
     * 解散
     */
    @DeleteMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=disbandGroup")
    public ResVo<?> disbandGroup(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") Long groupId) {
        return ResVo.success();
    }

    /**
     * 退群
     * */
    @DeleteMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=leaveGroup")
    public ResVo<?> leaveGroup(@RequestBody LeaveGroupReq req) {
        return ResVo.success();
    }

    /**
     * 加群
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=joinGroup")
    public ResVo<?> joinGroup(@RequestBody JoinGroupReq req) {
        return ResVo.success();
    }

    /**
     * 申请
     * */
    @PostMapping(value = "/Tenants/{tenantId}/Groups/{groupId}", params = "action=applyToJoinGroup")
    public ResVo<?> applyToJoinGroup(@RequestBody ApplyToJoinGroupReq req) {
        return ResVo.success();
    }

    /**
     * 群成员
     * */
    @GetMapping("/Tenants/{tenantId}/Groups/{groupId}/Members")
    public ResVo<?> findGroupMembers(@PathVariable("tenantId") Long tenantId, @PathVariable("groupId") Long groupId) {
        return ResVo.success();
    }

    /**
     * 我的群
     * */
    @GetMapping("/Tenants/{tenantId}/Groups")
    public ResVo<?> findGroups(FindGroupsReq req) {
        return ResVo.success();
    }


}
