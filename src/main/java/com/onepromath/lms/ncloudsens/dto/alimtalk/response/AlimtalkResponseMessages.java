package com.onepromath.lms.ncloudsens.dto.alimtalk.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlimtalkResponseMessages { // 알림톡 발송 응답 Model 내부 messages
    private String messageId;
    private String requestStatusCode;
    private String requestStatusName;
    private String requestStatusDesc;
    private boolean useSmsFailover;
}
