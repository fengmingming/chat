package boluo.chat.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ControlMessage extends Message {

    @NotBlank(message = "command is blank")
    private String command;
    private List<Object> args;

    /**
     * 审核群申请命令通知
     * */
    public static final String REVIEW_GROUP_JOIN_REQUEST_NOTIFICATION = "reviewGroupJoinRequestNotification";

    /**
     * 审核加好友命令通知
     * */
    public static final String REVIEW_FRIEND_REQUEST_NOTIFICATION = "reviewFriendRequestNotification";


}
