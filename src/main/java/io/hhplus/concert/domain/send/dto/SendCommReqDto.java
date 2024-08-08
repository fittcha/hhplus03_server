package io.hhplus.concert.domain.send.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendCommReqDto {

    private DataType type;
    private String jsonData;

    public SendCommReqDto(DataType type, String jsonData) {
        this.type = type;
        this.jsonData = jsonData;
    }

    public enum DataType {
        RESERVATION,
        CANCEL
    }
}
