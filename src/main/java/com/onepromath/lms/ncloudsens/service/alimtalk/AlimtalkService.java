package com.onepromath.lms.ncloudsens.service.alimtalk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onepromath.lms.ncloudsens.dto.alimtalk.request.AlimtalkRequestBody;
import com.onepromath.lms.ncloudsens.dto.alimtalk.request.AlimtalkRequestFailoverConfig;
import com.onepromath.lms.ncloudsens.dto.alimtalk.request.AlimtalkRequestMessages;
import com.onepromath.lms.ncloudsens.dto.alimtalk.response.AlimtalkResponseBody;
import com.onepromath.lms.ncloudsens.dto.alimtalk.result.response.AlimtalkResultResponseBody;
import com.onepromath.lms.ncloudsens.mapper.AlimtalkMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AlimtalkService { // 알림톡 서비스
    @Value("${ncloud.accessKey}")
    private String accessKey;

    @Value("${ncloud.secretKey}")
    private String secretKey;

    @Value("${ncloud.alimtalk.serviceId}")
    private String serviceId;

    @Value("${ncloud.alimtalk.plusFriendId}")
    private String plusFriendId;

    @Value("${ncloud.alimtalk.messages.countryCode}")
    private String countryCode;

    @Value("${ncloud.alimtalk.paidAcctMonthlyReport.templateCode}")
    private String paidAcctMonthlyReportTemplateCode;

    @Value("${ncloud.alimtalk.paidAcctMonthlyReport.messages.content1}")
    private String paidAcctMonthlyReportContent1;

    @Value("${ncloud.alimtalk.paidAcctMonthlyReport.messages.content2}")
    private String paidAcctMonthlyReportContent2;

    @Value("${ncloud.alimtalk.paidAcctMonthlyReport.messages.failoverConfig.content1}")
    private String paidAcctMonthlyReportFailoverConfigContent1;

    @Value("${ncloud.alimtalk.paidAcctMonthlyReport.messages.failoverConfig.content2}")
    private String paidAcctMonthlyReportFailoverConfigContent2;

    @Value("${ncloud.alimtalk.promo3DayMonthlyReport.templateCode}")
    private String promo3DayMonthlyReportTemplateCode;

    @Value("${ncloud.alimtalk.promo3DayMonthlyReport.messages.content1}")
    private String promo3DayMonthlyReportContent1;

    @Value("${ncloud.alimtalk.promo3DayMonthlyReport.messages.content2}")
    private String promo3DayMonthlyReportContent2;

    private final AlimtalkMapper alimtalkMapper;

    public AlimtalkService(AlimtalkMapper alimtalkMapper) {
        this.alimtalkMapper = alimtalkMapper;
    }

    // 유료 계정 월간 보고서 알림톡 발송
    public AlimtalkResultResponseBody sendPaidAcctMonthlyReport(String phoneNumber, String url) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        Long time = System.currentTimeMillis();

        AlimtalkRequestFailoverConfig alimtalkRequestFailoverConfig = new AlimtalkRequestFailoverConfig(this.paidAcctMonthlyReportFailoverConfigContent1 + url + this.paidAcctMonthlyReportFailoverConfigContent2);
        List<AlimtalkRequestMessages> alimtalkRequestMessages = new ArrayList<>();
        alimtalkRequestMessages.add(new AlimtalkRequestMessages(this.countryCode, phoneNumber, this.paidAcctMonthlyReportContent1 + url + this.paidAcctMonthlyReportContent2, true, alimtalkRequestFailoverConfig));
        AlimtalkRequestBody alimtalkRequestBody = new AlimtalkRequestBody(this.paidAcctMonthlyReportTemplateCode, this.plusFriendId, alimtalkRequestMessages);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(alimtalkRequestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", this.accessKey);
        String signature = makeSendSignature(time);
        headers.set("x-ncp-apigw-signature-v2", signature);

        HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        AlimtalkResponseBody alimtalkResponseBody = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/alimtalk/v2/services/" + this.serviceId + "/messages"), body, AlimtalkResponseBody.class);
        AlimtalkResultResponseBody alimtalkResultResponseBody = sendResult(alimtalkResponseBody.getMessages().get(0).getMessageId());

        return alimtalkResultResponseBody;
    }

    // 프로모션 3일차 월간 보고서 알림톡 발송
    public AlimtalkResultResponseBody sendPromo3DayMonthlyReport(String phoneNumber, String url) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
        Long time = System.currentTimeMillis();

        AlimtalkRequestFailoverConfig alimtalkRequestFailoverConfig = new AlimtalkRequestFailoverConfig();
        List<AlimtalkRequestMessages> alimtalkRequestMessages = new ArrayList<>();
        alimtalkRequestMessages.add(new AlimtalkRequestMessages(this.countryCode, phoneNumber, this.promo3DayMonthlyReportContent1 + url + this.promo3DayMonthlyReportContent2, false, alimtalkRequestFailoverConfig));
        AlimtalkRequestBody alimtalkRequestBody = new AlimtalkRequestBody(this.promo3DayMonthlyReportTemplateCode, this.plusFriendId, alimtalkRequestMessages);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(alimtalkRequestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", this.accessKey);
        String signature = makeSendSignature(time);
        headers.set("x-ncp-apigw-signature-v2", signature);

        HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        AlimtalkResponseBody alimtalkResponseBody = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/alimtalk/v2/services/" + this.serviceId + "/messages"), body, AlimtalkResponseBody.class);
        AlimtalkResultResponseBody alimtalkResultResponseBody = sendResult(alimtalkResponseBody.getMessages().get(0).getMessageId());

        return alimtalkResultResponseBody;
    }

    // 알림톡 발송 결과
    public AlimtalkResultResponseBody sendResult(String messageId) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        Long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", this.accessKey);
        String signature = makeSendResultSignature(time, messageId);
        headers.set("x-ncp-apigw-signature-v2", signature);

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<AlimtalkResultResponseBody> responseEntity = restTemplate.exchange(
                "https://sens.apigw.ntruss.com/alimtalk/v2/services/" + this.serviceId + "/messages/" + messageId,
                HttpMethod.GET,
                httpEntity,
                AlimtalkResultResponseBody.class
        );

        return responseEntity.getBody();
    }

    // 알림톡 발송 시그니처 생성
    public String makeSendSignature(Long time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/alimtalk/v2/services/" + this.serviceId + "/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    // 알림톡 발송 결과 시그니처 생성
    public String makeSendResultSignature(Long time, String messageId) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "GET";
        String url = "/alimtalk/v2/services/" + this.serviceId + "/messages/" + messageId;
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }
}
