package com.onepromath.lms.ncloudsens.mapper.schedule;

import com.onepromath.lms.ncloudsens.dto.learning.report.PaidAcctMonthlyReportListDto;
import com.onepromath.lms.ncloudsens.dto.learning.report.Promo3DayMonthlyReportListDto;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface LearningReportListMapper {
    // 유료 계정 월간 보고서 목록
    @Select("select replace(li.phone, '-', '') phone_number,\n" +
            "       li.NO                      user_number,\n" +
            "       (select NO\n" +
            "        from LOGIN_ID_PROFILE lip\n" +
            "        where lip.LOGIN_ID_NO = lia.LOGIN_ID_NO\n" +
            "          and lip.use_yn = 'y'\n" +
            "        limit 1)                  profile_number\n" +
            "from (select *\n" +
            "      from (select *\n" +
            "            from LOGIN_ID_ACTIVE\n" +
            "            where e_date >= date_format(now(), '%Y-%m-%d')\n" +
            "              and use_yn = 'y'\n" +
            "            order by LOGIN_ID_NO, e_date desc\n" +
            "            limit 18446744073709551615) t1\n" +
            "      group by LOGIN_ID_NO) lia\n" +
            "         left join LOGIN_ID li on li.NO = lia.LOGIN_ID_NO\n" +
            "where li.use_yn = 'y'\n" +
            "  and li.school_id_yn = 'n'\n" +
            "  and li.phone is not null\n" +
            "  and replace(replace(li.phone, '-', ''), ' ', '') != ''\n" +
            "  and char_length(replace(replace(li.phone, '-', ''), ' ', '')) = 11\n" +
            "group by replace(li.phone, '-', ''), li.NO;")
    @Results(id = "paidAcctMonthlyReportListMap", value = {
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "userNumber", column = "user_number"),
            @Result(property = "profileNumber", column = "profile_number")
    })
    ArrayList<PaidAcctMonthlyReportListDto> getPaidAcctMonthlyReportList();

    // 프로모션 3일차 월간 보고서 목록
    @Select("select replace(li.phone, '-', '') phone_number, lip.NO profile_number\n" +
            "from PROMOTION promo\n" +
            "         left join LOGIN_ID li on li.NO = promo.LOGIN_ID_NO\n" +
            "         left join LOGIN_ID_PROFILE lip on li.NO = lip.LOGIN_ID_NO\n" +
            "where promo.show_yn = 'y'\n" +
            "  and date_format(promo.end_date, '%Y-%m-%d') = date_format(date_add(now(), interval +1 day), '%Y-%m-%d')\n" +
            "  and li.school_id_yn = 'n'\n" +
            "  and li.use_yn = 'y'\n" +
            "  and li.phone is not null\n" +
            "  and replace(replace(li.phone, '-', ''), ' ', '') != ''\n" +
            "  and char_length(replace(replace(li.phone, '-', ''), ' ', '')) = 11\n" +
            "group by replace(li.phone, '-', ''), lip.NO;")
    @Results(id = "promo3DayMonthlyReportListMap", value = {
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "profileNumber", column = "profile_number")
    })
    ArrayList<Promo3DayMonthlyReportListDto> getPromo3DayMonthlyReportList();

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
