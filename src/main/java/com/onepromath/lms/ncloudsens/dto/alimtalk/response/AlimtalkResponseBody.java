package com.onepromath.lms.ncloudsens.dto.alimtalk.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class AlimtalkResponseBody { // 알림톡 발송 응답 Model
    private String requestId;
    private String requestTime;
    private List<AlimtalkResponseMessages> messages;
}
