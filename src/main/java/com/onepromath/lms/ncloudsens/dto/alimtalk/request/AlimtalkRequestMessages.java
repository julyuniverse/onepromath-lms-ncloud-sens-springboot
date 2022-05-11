package com.onepromath.lms.ncloudsens.dto.alimtalk.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlimtalkRequestMessages { // 알림톡 발송 요청 Model 내부 messages
    private String countryCode; // 국가 코드
    private String to; // 수신자 전화번호
    private String content; // 알림톡 내용
    private AlimtalkRequestFailoverConfig failoverConfig; // Failover 설정
}
