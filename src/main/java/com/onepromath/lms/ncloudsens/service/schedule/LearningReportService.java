package com.onepromath.lms.ncloudsens.service.schedule;

import com.onepromath.lms.ncloudsens.service.crypto.Aes256;
import com.onepromath.lms.ncloudsens.dto.learning.report.LearningReportDto;
import com.onepromath.lms.ncloudsens.dto.alimtalk.result.response.AlimtalkResultResponseBody;
import com.onepromath.lms.ncloudsens.mapper.schedule.LearningReportMapper;
import com.onepromath.lms.ncloudsens.service.alimtalk.AlimtalkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

@Service
@Transactional
public class LearningReportService { // 학습 보고서 스케줄 서비스
    private final LearningReportMapper learningReportMapper;
    private final AlimtalkService alimtalkService;
    private final Aes256 aes256;

    public LearningReportService(LearningReportMapper learningReportMapper, AlimtalkService alimtalkService, Aes256 aes256) {
        this.learningReportMapper = learningReportMapper;
        this.alimtalkService = alimtalkService;
        this.aes256 = aes256;
    }

    // 유료 계정 월간 보고서 알림톡 발송 (매월 1일 오후 12시 발송)
    public void sendPaidAcctMonthlyReportListAlimtalk() throws Exception {
        int successCount = 0;
        int failCount = 0;
        int totalCount = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // 지난달 1일로 날짜 설정
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, -1);
        String startDate = format.format(calendar.getTime());

        // 유료 계정 월간 보고서 목록 가져오기.
        ArrayList<LearningReportDto> learningReportDtoArrayList;
        learningReportDtoArrayList = learningReportMapper.getPaidAcctMonthlyReportList();

        for (LearningReportDto learningReportDto : learningReportDtoArrayList) {
            String userNumber = URLEncoder.encode(aes256.encrypt(learningReportDto.getUserNumber()), "UTF-8");
            String profileNumber = URLEncoder.encode(aes256.encrypt(learningReportDto.getProfileNumber()), "UTF-8");
            String url = "http://일프로선생님.kr/monthly-report/21/" + userNumber + "/" + profileNumber + "/" + startDate + "";

            if (learningReportDto.getLearningCountAMonthAgo() > 0 && learningReportDto.getLearningCountTwoMonthsAgo() > 0
                    || learningReportDto.getLearningCountAMonthAgo() > 0 && learningReportDto.getLearningCountTwoMonthsAgo() == 0) { // 한 달 전 학습량과 2달 전 학습량이 모두 있거나 한 달 전 학습량은 있고 2달 전 학습량은 없는 경우 기본 알림톡 발송
                AlimtalkResultResponseBody alimtalkResultResponseBody = alimtalkService.sendPaidAcctMonthlyReport(learningReportDto.getPhoneNumber(), url);

                if (Objects.equals(alimtalkResultResponseBody.getRequestStatusName(), "success")) {
                    successCount++;
                } else {
                    failCount++;
                }
            } else if (learningReportDto.getLearningCountAMonthAgo() == 0 && learningReportDto.getLearningCountTwoMonthsAgo() > 0
                    || learningReportDto.getLearningCountAMonthAgo() == 0 && learningReportDto.getPaymentStatusAMonthAgo() == 1) { // 한 달 전 학습량은 없고 2달 전 학습량은 있는 경우와 한 달 전 학습량은 없고 한 달 전 최초 결제일 경우 안내 내용이 있는 알림톡 발송
                AlimtalkResultResponseBody alimtalkResultResponseBody = alimtalkService.sendPaidAcctOneMonthMonthlyReport(learningReportDto.getPhoneNumber(), url);

                if (Objects.equals(alimtalkResultResponseBody.getRequestStatusName(), "success")) {
                    successCount++;
                } else {
                    failCount++;
                }
            }

//            else { // 나머지는 알림톡 발송 X (2022년 7월 21일 현시점으로 2개월 연속으로 데이터가 없는 계정이 있을 수도 있다. 2개월 연속 데이터가 없는 계정은 1개월 경고 알림 내용 알림톡 자체를 받을 수 없다. 그러므로 새로운 로직으로 변경 시 1회만 발송하도록 하고 해당 else문 로직은 2022년 8월 1일 발송 이후 삭제해야 함.)
//                AlimtalkResultResponseBody alimtalkResultResponseBody = alimtalkService.sendPaidAcctOneMonthMonthlyReport(learningReportDto.getPhoneNumber(), url);
//
//                if (Objects.equals(alimtalkResultResponseBody.getRequestStatusName(), "success")) {
//                    successCount++;
//                } else {
//                    failCount++;
//                }
//            }

            totalCount++;
        }

        learningReportMapper.createLearningReportListAlimtalkLog(11, successCount, failCount, totalCount);
    }

    // 프로모션 3일차 월간 보고서 알림톡 발송 (매일 오후 12시)
    public void sendPromo3DayMonthlyReportListAlimtalk() throws Exception {
        int successCount = 0;
        int failCount = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // 이달 1일로 날짜 설정
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startDate = format.format(calendar.getTime());

        // 프로모션 3일차 월간 보고서 목록 가져오기.
        ArrayList<LearningReportDto> learningReportDtoArrayList;
        learningReportDtoArrayList = learningReportMapper.getPromo3DayMonthlyReportList();

        for (LearningReportDto learningReportDto : learningReportDtoArrayList) {
            String userNumber = URLEncoder.encode(aes256.encrypt(learningReportDto.getUserNumber()), "UTF-8");
            String profileNumber = URLEncoder.encode(aes256.encrypt(learningReportDto.getProfileNumber()), "UTF-8");
            String url = "http://일프로선생님.kr/monthly-report/32/" + userNumber + "/" + profileNumber + "/" + startDate + "";
            AlimtalkResultResponseBody alimtalkResultResponseBody = alimtalkService.sendPromo3DayMonthlyReport(learningReportDto.getPhoneNumber(), url);

            if (Objects.equals(alimtalkResultResponseBody.getRequestStatusName(), "success")) {
                successCount++;
            } else {
                failCount++;
            }
        }

        learningReportMapper.createLearningReportListAlimtalkLog(12, successCount, failCount, successCount + failCount);
    }
}