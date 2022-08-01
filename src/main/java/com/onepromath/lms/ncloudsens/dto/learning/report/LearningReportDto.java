package com.onepromath.lms.ncloudsens.dto.learning.report;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class LearningReportDto {
    private String phoneNumber; // 전화번호
    private String userNumber; // 유저 넘버
    private String profileNumber; // 유저 프로필 넘버
    private int learningCountAMonthAgo; // 한 달 전 학습량
    private int learningCountTwoMonthsAgo; // 2달 전 학습량
    private int paymentStatusAMonthAgo; // 한 달 전 결제 상태
}
