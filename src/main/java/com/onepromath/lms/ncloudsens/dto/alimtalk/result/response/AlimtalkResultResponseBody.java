package com.onepromath.lms.ncloudsens.dto.alimtalk.result.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AlimtalkResultResponseBody { // 알림톡 발송 결과 Model
    private String messageId; // 메시지 아이디
    private String requestId; // 발송 요청 아이디
    private String requestTime; // 발송 요청 시간
    private String completeTime; // 발송 리포트(처리 완료) 시간
    private String plusFriendId; // 카카오톡 채널명 ((구)플러스친구 아이디)
    private String templateCode; // 템플릿 코드
    private String countryCode; // 수신자 국가번호
    private String to; // 수신자 번호
    private String content; // 알림톡 메시지 내용
    private String requestStatusCode; // 발송 요청 상태 코드 (A000: 성공, 그 외 코드: 실패)
    private String requestStatusName; // 발송 요청 상태명 (success: 성공, fail: 실패)
    private String requestStatusDesc; // 발송 요청 상태 내용
    private String messageStatusCode; // 발송 결과 상태 코드 (0000: 성공, 그 외 코드: 실패)
    private String messageStatusName; // 발송 결과 상태명
    private String messageStatusDesc; // 발송 결과 상태 내용
    private boolean useSmsFailover; // SMS Failover 사용 여부
}
