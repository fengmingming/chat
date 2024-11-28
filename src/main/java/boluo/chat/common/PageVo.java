package boluo.chat.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageVo<T> {

    private Long total;
    private List<T> records;

    @JsonCreator
    public PageVo(@JsonProperty("total") Long total, @JsonProperty("records") List<T> records) {
        this.total = total;
        this.records = records;
    }

}
