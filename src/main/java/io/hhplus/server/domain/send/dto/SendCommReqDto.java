package io.hhplus.server.domain.send.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendCommReqDto {

    private Long sendId;
    private String jsonData;

    public SendCommReqDto(Long sendId, String jsonData) {
        this.sendId = sendId;
        this.jsonData = jsonData;
    }
}
