package boluo.chat.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "msgType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringMessage.class, name = "text"),//文字
        @JsonSubTypes.Type(value = ImageMessage.class, name = "image"),//图片
        @JsonSubTypes.Type(value = VideoMessage.class, name = "video"),//视频
        @JsonSubTypes.Type(value = FileMessage.class, name = "file"),//视频
        @JsonSubTypes.Type(value = FaceMessage.class, name = "face"),//视频
        @JsonSubTypes.Type(value = LocationMessage.class, name = "location"),//视频
        @JsonSubTypes.Type(value = SoundMessage.class, name = "sound"),//视频
        @JsonSubTypes.Type(value = ControlMessage.class, name = "control"),//控制
        @JsonSubTypes.Type(value = CustomMessage.class, name = "custom"),//自定义
        @JsonSubTypes.Type(value = RelayMessage.class, name = "relay")//转发消息
})
@JsonIgnoreProperties(ignoreUnknown = true, value = {"msgType"})
public abstract class Message {

    private String msgId;
    @NotBlank(message = "tenantId is blank")
    private String tenantId;
    @NotBlank(message = "from is blank")
    private String from;
    @NotBlank(message = "to is blank")
    private String to;
    private Long timestamp;

    public boolean isGroup() {
        return to != null && to.startsWith("GROUP:");
    }

    public String getGroupId() {
        if(isGroup()) {
            return to.substring(6).trim();
        }
        return null;
    }

    public String findMsgType() {
        if(this instanceof StringMessage) {
            return "text";
        }else if(this instanceof ImageMessage) {
            return "image";
        }else if(this instanceof VideoMessage) {
            return "video";
        }else if(this instanceof FileMessage) {
            return "file";
        }else if(this instanceof FaceMessage) {
            return "face";
        }else if(this instanceof LocationMessage) {
            return "location";
        }else if(this instanceof SoundMessage) {
            return "sound";
        }else if(this instanceof ControlMessage) {
            return "control";
        }else if(this instanceof CustomMessage) {
            return "custom";
        }else if(this instanceof RelayMessage) {
            return "relay";
        }else {
            throw new IllegalArgumentException("unregistered message:" + this.getClass().getSimpleName());
        }
    }

}
