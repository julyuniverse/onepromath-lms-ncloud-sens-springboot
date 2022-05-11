package com.onepromath.lms.ncloudsens.dto.alimtalk.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlimtalkRequestFailoverConfig { // 알림톡 발송 요청 Model 내부 messages 내부 failoverConfig
    private String content; // 문자 메시지 내용
}
