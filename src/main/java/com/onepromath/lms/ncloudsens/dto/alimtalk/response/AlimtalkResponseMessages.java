package com.onepromath.lms.ncloudsens.dto.alimtalk.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlimtalkResponseMessages { // 알림톡 발송 응답 Model 내부 messages
    private String messageId;
    private String requestStatusCode;
    private String requestStatusName;
    private String requestStatusDesc;
    private boolean useSmsFailover;
}
