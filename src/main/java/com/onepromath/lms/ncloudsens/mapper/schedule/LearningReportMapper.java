package com.onepromath.lms.ncloudsens.mapper.schedule;

import com.onepromath.lms.ncloudsens.dto.learning.report.LearningReportDto;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface LearningReportMapper {
    // 유료 계정 월간 보고서 목록
    @Select("select replace(li.phone, '-', '') as phone_number,\n" +
            "       li.NO                      as user_number,\n" +
            "       (select NO\n" +
            "        from LOGIN_ID_PROFILE lip\n" +
            "        where lip.LOGIN_ID_NO = lia.LOGIN_ID_NO\n" +
            "          and lip.use_yn = 'y'\n" +
            "        limit 1)                  as profile_number,\n" +
            "       ((select count(*)\n" +
            "         from RESULT_DAILY_2\n" +
            "         where LOGIN_ID_PROFILE_NO in\n" +
            "               (select NO from LOGIN_ID_PROFILE where LOGIN_ID_NO = lia.LOGIN_ID_NO and use_yn = 'y')\n" +
            "           and start_time >= date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 1 month)\n" +
            "           and start_time < concat(date_format(now(), '%Y-%m'), '-01')\n" +
            "           and use_yn = 'y') +\n" +
            "        (select count(*)\n" +
            "         from RESULT_FREE_2\n" +
            "         where LOGIN_ID_PROFILE_NO in\n" +
            "               (select NO from LOGIN_ID_PROFILE where LOGIN_ID_NO = lia.LOGIN_ID_NO and use_yn = 'y')\n" +
            "           and start_time >= date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 1 month)\n" +
            "           and start_time < concat(date_format(now(), '%Y-%m'), '-01')\n" +
            "           and use_yn = 'y') +\n" +
            "        (select count(*)\n" +
            "         from RESULT_ONEPRO_2\n" +
            "         where LOGIN_ID_PROFILE_NO in\n" +
            "               (select NO from LOGIN_ID_PROFILE where LOGIN_ID_NO = lia.LOGIN_ID_NO and use_yn = 'y')\n" +
            "           and start_time >= date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 1 month)\n" +
            "           and start_time < concat(date_format(now(), '%Y-%m'), '-01')\n" +
            "           and use_yn = 'y') +\n" +
            "        (select count(*)\n" +
            "         from RESULT_WORLD_2\n" +
            "         where LOGIN_ID_PROFILE_NO in\n" +
            "               (select NO from LOGIN_ID_PROFILE where LOGIN_ID_NO = lia.LOGIN_ID_NO and use_yn = 'y')\n" +
            "           and start_time >= date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 1 month)\n" +
            "           and start_time < concat(date_format(now(), '%Y-%m'), '-01')\n" +
            "           and use_yn = 'y'))     as learning_count_a_month_ago,\n" +
            "       ((select count(*)\n" +
            "         from RESULT_DAILY_2\n" +
            "         where LOGIN_ID_PROFILE_NO in\n" +
            "               (select NO from LOGIN_ID_PROFILE where LOGIN_ID_NO = lia.LOGIN_ID_NO and use_yn = 'y')\n" +
            "           and start_time >= date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 2 month)\n" +
            "           and start_time < date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 1\n" +
            "                                     month)\n" +
            "           and use_yn = 'y') +\n" +
            "        (select count(*)\n" +
            "         from RESULT_FREE_2\n" +
            "         where LOGIN_ID_PROFILE_NO in\n" +
            "               (select NO from LOGIN_ID_PROFILE where LOGIN_ID_NO = lia.LOGIN_ID_NO and use_yn = 'y')\n" +
            "           and start_time >= date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 2 month)\n" +
            "           and start_time < date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 1\n" +
            "                                     month)\n" +
            "           and use_yn = 'y') +\n" +
            "        (select count(*)\n" +
            "         from RESULT_ONEPRO_2\n" +
            "         where LOGIN_ID_PROFILE_NO in\n" +
            "               (select NO from LOGIN_ID_PROFILE where LOGIN_ID_NO = lia.LOGIN_ID_NO and use_yn = 'y')\n" +
            "           and start_time >= date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 2 month)\n" +
            "           and start_time < date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 1\n" +
            "                                     month)\n" +
            "           and use_yn = 'y') +\n" +
            "        (select count(*)\n" +
            "         from RESULT_WORLD_2\n" +
            "         where LOGIN_ID_PROFILE_NO in\n" +
            "               (select NO from LOGIN_ID_PROFILE where LOGIN_ID_NO = lia.LOGIN_ID_NO and use_yn = 'y')\n" +
            "           and start_time >= date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 2 month)\n" +
            "           and start_time < date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 1\n" +
            "                                     month)\n" +
            "           and use_yn = 'y'))     as learning_count_two_months_ago,\n" +
            "       if((select date_format(s_date, '%Y-%m')\n" +
            "           from LOGIN_ID_ACTIVE\n" +
            "           where LOGIN_ID_NO = lia.LOGIN_ID_NO\n" +
            "             and use_yn = 'y'\n" +
            "           order by s_date\n" +
            "           limit 1) = date_format(date_sub(concat(date_format(now(), '%Y-%m'), '-01'), interval 1 month), '%Y-%m'), 1,\n" +
            "          0)                      as payment_status_a_month_ago\n" +
            "from (select LOGIN_ID_NO\n" +
            "      from LOGIN_ID_ACTIVE\n" +
            "      where e_date >= date_format(now(), '%Y-%m-%d')\n" +
            "        and use_yn = 'y'\n" +
            "      group by LOGIN_ID_NO) as lia\n" +
            "         left join LOGIN_ID as li on li.NO = lia.LOGIN_ID_NO\n" +
            "where li.use_yn = 'y'\n" +
            "  and li.school_id_yn = 'n'\n" +
            "  and li.phone is not null\n" +
            "  and replace(replace(li.phone, '-', ''), ' ', '') != ''\n" +
            "  and char_length(replace(replace(li.phone, '-', ''), ' ', '')) = 11;")
    @Results(id = "learningReportMap", value = {
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "userNumber", column = "user_number"),
            @Result(property = "profileNumber", column = "profile_number"),
            @Result(property = "learningCountAMonthAgo", column = "learning_count_a_month_ago"),
            @Result(property = "learningCountTwoMonthsAgo", column = "learning_count_two_months_ago"),
            @Result(property = "paymentStatusAMonthAgo", column = "payment_status_a_month_ago")
    })
    ArrayList<LearningReportDto> getPaidAcctMonthlyReportList();

    // 프로모션 3일차 월간 보고서 목록
    @Select("select replace(li.phone, '-', '') as phone_number, li.NO as user_number, lip.NO as profile_number\n" +
            "from PROMOTION promo\n" +
            "         left join LOGIN_ID li on li.NO = promo.LOGIN_ID_NO\n" +
            "         left join LOGIN_ID_PROFILE lip on li.NO = lip.LOGIN_ID_NO\n" +
            "where promo.show_yn = 'y'\n" +
            "  and date_format(promo.end_date, '%Y-%m-%d') = date_format(date_add(now(), interval 1 day), '%Y-%m-%d')\n" +
            "  and li.use_yn = 'y'\n" +
            "  and li.school_id_yn = 'n'\n" +
            "  and li.phone is not null\n" +
            "  and replace(replace(li.phone, '-', ''), ' ', '') != ''\n" +
            "  and char_length(replace(replace(li.phone, '-', ''), ' ', '')) = 11\n" +
            "group by replace(li.phone, '-', ''), lip.NO;")
    @ResultMap("learningReportMap")
    ArrayList<LearningReportDto> getPromo3DayMonthlyReportList();

    // 학습 보고서 알림톡 발송 로그
    @Insert("insert into LEARNING_REPORT_MESSAGE_SEND_LOG (send_type, success_count, fail_count, total_count)\n" +
            "values (#{send_type}, #{success_count}, #{fail_count}, #{total_count});")
    int createLearningReportListAlimtalkLog(
            @Param("send_type") int sendType,
            @Param("success_count") int successCount,
            @Param("fail_count") int failCount,
            @Param("total_count") int totalCount
    );
}
