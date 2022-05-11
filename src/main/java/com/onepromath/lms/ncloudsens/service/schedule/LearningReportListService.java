package com.onepromath.lms.ncloudsens.service.schedule;

import com.onepromath.lms.ncloudsens.dto.learning.report.Promo3DayMonthlyReportListDto;
import com.onepromath.lms.ncloudsens.service.crypto.Aes256;
import com.onepromath.lms.ncloudsens.dto.learning.report.PaidAcctMonthlyReportListDto;
import com.onepromath.lms.ncloudsens.dto.alimtalk.result.response.AlimtalkResultResponseBody;
import com.onepromath.lms.ncloudsens.mapper.schedule.LearningReportListMapper;
import com.onepromath.lms.ncloudsens.service.alimtalk.AlimtalkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

@Service
@Transactional
public class LearningReportListService { // 학습 보고서 목록 스케줄 서비스
    private final LearningReportListMapper learningReportListMapper;
    private final AlimtalkService alimtalkService;
    private final Aes256 aes256;

    public LearningReportListService(LearningReportListMapper learningReportListMapper, AlimtalkService alimtalkService, Aes256 aes256) {
        this.learningReportListMapper = learningReportListMapper;
        this.alimtalkService = alimtalkService;
        this.aes256 = aes256;
    }

    // 유료 계정 월간 보고서 알림톡 발송 (매월 1일 오후 12시 발송)
    public void sendPaidAcctMonthlyReportListAlimtalk() throws Exception {
        int successCount = 0;
        int failCount = 0;

        LocalDate now = LocalDate.now();
        LocalDate oneMonthAgo = now.minusMonths(1);
        int year = oneMonthAgo.getYear();
        int month = oneMonthAgo.getMonthValue() - 1;

        // 유료 계정 월간 보고서 목록 가져오기.
        ArrayList<PaidAcctMonthlyReportListDto> paidAcctMonthlyReportListDtoArrayList;
        paidAcctMonthlyReportListDtoArrayList = learningReportListMapper.getPaidAcctMonthlyReportList();

        for (PaidAcctMonthlyReportListDto paidAcctMonthlyReportListDto : paidAcctMonthlyReportListDtoArrayList) {
            String userNumber = URLEncoder.encode(aes256.encrypt(paidAcctMonthlyReportListDto.getUserNumber()), "UTF-8");
            String profileNumber = URLEncoder.encode(aes256.encrypt(paidAcctMonthlyReportListDto.getProfileNumber()), "UTF-8");
            String url = "http://일프로선생님.kr/monthlyreport/21/" + userNumber + "/" + profileNumber + "/" + year + "/" + month + "";
            AlimtalkResultResponseBody alimtalkResultResponseBody = alimtalkService.sendPaidAcctMonthlyReport(paidAcctMonthlyReportListDto.getPhoneNumber(), paidAcctMonthlyReportListDto.getUserNumber(), paidAcctMonthlyReportListDto.getProfileNumber(), url);

            if(Objects.equals(alimtalkResultResponseBody.getRequestStatusName(), "success")) {
                successCount++;
            }else{
                failCount++;
            }
        }

        learningReportListMapper.createLearningReportListAlimtalkLog(11, successCount, failCount, successCount + failCount);
    }

    // 프로모션 3일차 월간 보고서 알림톡 발송 (매일 오후 12시)
    public void sendPromo3DayMonthlyReportListAlimtalk() throws Exception {
        int successCount = 0;
        int failCount = 0;

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue() - 1;

        // 프로모션 3일차 월간 보고서 목록 가져오기.
        ArrayList<Promo3DayMonthlyReportListDto> promo3DayMonthlyReportListDtoArrayList;
        promo3DayMonthlyReportListDtoArrayList = learningReportListMapper.getPromo3DayMonthlyReportList();

        for (Promo3DayMonthlyReportListDto promo3DayMonthlyReportListDto : promo3DayMonthlyReportListDtoArrayList) {
            String profileNumber = URLEncoder.encode(aes256.encrypt(promo3DayMonthlyReportListDto.getProfileNumber()), "UTF-8");
            String url = "http://일프로선생님.kr/monthlyreport/32/0/" + profileNumber + "/" + year + "/" + month + "";
            AlimtalkResultResponseBody alimtalkResultResponseBody = alimtalkService.sendPromo3DayMonthlyReport(promo3DayMonthlyReportListDto.getPhoneNumber(), promo3DayMonthlyReportListDto.getProfileNumber(), url);

            if(Objects.equals(alimtalkResultResponseBody.getRequestStatusName(), "success")) {
                successCount++;
            }else{
                failCount++;
            }
        }

        learningReportListMapper.createLearningReportListAlimtalkLog(12, successCount, failCount, successCount + failCount);
    }
}