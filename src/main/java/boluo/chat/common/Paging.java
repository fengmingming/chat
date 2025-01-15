package boluo.chat.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Paging {

    @Max(value = 1000, message = "the max pageSize is 1000")
    @Min(value = 1, message = "the min pageSize is 1")
    private int pageSize = 1000;
    @Min(value = 1, message = "the min pageNum is 1")
    private int pageNum = 1;

}
