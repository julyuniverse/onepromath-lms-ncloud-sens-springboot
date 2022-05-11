package com.onepromath.lms.ncloudsens.schedule;

import com.onepromath.lms.ncloudsens.service.schedule.LearningReportListService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LearningReportList { // 학습 보고서 목록 스케줄
    private final LearningReportListService learningReportListService;

    public LearningReportList(LearningReportListService learningReportListService) {
        this.learningReportListService = learningReportListService;
    }

    @Scheduled(cron = "0 0 12 1 * ?") // 매월 1일 오후 12시 유료 계정 월간 보고서 목록 발송
    public void sendPaidAcctMonthlyReportAlimtalk() throws Exception {
        learningReportListService.sendPaidAcctMonthlyReportListAlimtalk();
    }

    @Scheduled(cron = "0 0 12 * * ?") // 매일 오후 12시 프로모션 3일차 월간 보고서 목록 발송
    public void sendPromo3DayMonthlyReportAlimtalk() throws Exception {
        learningReportListService.sendPromo3DayMonthlyReportListAlimtalk();
    }
}
