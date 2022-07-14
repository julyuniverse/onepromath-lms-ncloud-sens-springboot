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
            AlimtalkResultResponseBody alimtalkResultResponseBody = alimtalkService.sendPaidAcctMonthlyReport(learningReportDto.getPhoneNumber(), url);

            if (Objects.equals(alimtalkResultResponseBody.getRequestStatusName(), "success")) {
                successCount++;
            } else {
                failCount++;
            }
        }

        learningReportMapper.createLearningReportListAlimtalkLog(11, successCount, failCount, successCount + failCount);
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