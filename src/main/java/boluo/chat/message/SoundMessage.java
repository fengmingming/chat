package boluo.chat.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SoundMessage extends Message {

    @NotBlank(message = "soundUUID is blank")
    private String soundUUID;//索引id
    @NotBlank(message = "soundUrl is blank")
    private String soundUrl; //地址
    @NotNull(message = "soundSize is null")
    private Long soundSize; //大小
    @NotNull(message = "soundSeconds is null")
    private Integer soundSeconds; //时长

    @Override
    public String getMsgType() {
        return "sound";
    }

}
