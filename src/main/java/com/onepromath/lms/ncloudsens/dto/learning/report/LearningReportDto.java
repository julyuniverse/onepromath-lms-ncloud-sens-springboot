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
}
