package com.onepromath.lms.ncloudsens.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AlimtalkMapper {
    // 로그 생성
    @Insert("insert into NSS_MESSAGE_SEND_LOG (NSS_ORDER_LIST_NO, message_id, request_id, request_time, complete_time, plus_friend_id, template_code, country_code, to_phone_number, request_status_code, request_status_name, request_status_desc, message_status_code, message_status_name, message_status_desc, use_sms_failover)" +
            "values (#{NSS_ORDER_LIST_NO}, #{message_id}, #{request_id}, #{request_time}, #{complete_time}, #{plus_friend_id}, #{template_code}, #{country_code}, #{to_phone_number}, #{request_status_code}, #{request_status_name}, #{request_status_desc}, #{message_status_code}, #{message_status_name}, #{message_status_desc}, #{use_sms_failover});")
    int createAlimtalkLog(
            @Param("NSS_ORDER_LIST_NO") int nssOrderListNo,
            @Param("message_id") String messageId,
            @Param("request_id") String requestId,
            @Param("request_time") String requestTime,
            @Param("complete_time") String completeTime,
            @Param("plus_friend_id") String plusFriendId,
            @Param("template_code") String templateCode,
            @Param("country_code") String countryCode,
            @Param("to_phone_number") String to,
            @Param("request_status_code") String requestStatusCode,
            @Param("request_status_name") String requestStatusName,
            @Param("request_status_desc") String requestStatusDesc,
            @Param("message_status_code") String messageStatusCode,
            @Param("message_status_name") String messageStatusName,
            @Param("message_status_desc") String messageStatusDesc,
            @Param("use_sms_failover") boolean useSmsFailover
    );

    // 발송 완료 처리
    @Update("update NSS_ORDER_LIST set message_send_yn = 'y' where NO = #{NSS_ORDER_LIST_NO};")
    int updateNssOrderListMessageSendY(@Param("NSS_ORDER_LIST_NO") int nssOrderListNo);
}
