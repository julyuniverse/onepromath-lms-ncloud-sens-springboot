package com.onepromath.lms.ncloudsens.dto.alimtalk.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlimtalkRequestBody { // 알림톡 발송 요청 Model
    private String templateCode;
    private String plusFriendId;
    private List<AlimtalkRequestMessages> messages;
}
