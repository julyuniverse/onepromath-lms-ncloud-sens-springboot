package com.onepromath.lms.ncloudsens.dto.learning.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaidAcctMonthlyReportListDto { // 유료 계정 월간 보고서 목록
    private String phoneNumber; // 전화번호
    private String userNumber; // 유저 넘버
    private String profileNumber; // 유저 프로필 넘버
}
