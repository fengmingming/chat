package boluo.chat.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FaceMessage extends Message {

    @NotBlank(message = "faceUUID is blank")
    private String faceUUID;//表情唯一标识

}
