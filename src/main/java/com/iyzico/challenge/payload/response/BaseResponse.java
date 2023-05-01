package com.iyzico.challenge.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
    private String returnCode;
    private String returnMessage;

}
