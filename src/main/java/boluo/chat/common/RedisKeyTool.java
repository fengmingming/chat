package boluo.chat.common;

public class RedisKeyTool {

    public static String accountTokenRedisKey(Long tenantId, String account) {
        return String.format("boluo:chat:token:tenant:%d:account:%s", tenantId, account);
    }

    public static String tenantTokenRedisKey(Long tenantId) {
        return "boluo:chat:token:tenant:"+tenantId;
    }

    public static String managerTokenRedisKey(Long managerId) {
        return "boluo:chat:token:manager:"+managerId;
    }

    public static String groupMemberRedisKey(Long tenantId, String groupId) {
        return String.format("boluo:chat:tenant:%d:group:%s:members", tenantId, groupId);
    }

}
