# Onepromath LMS - ncloud SENS 메시지 발송 API

> 메시지 발송 스케줄
>
> 유료 계정에 한해서 매월 1일 오후 12시 학습 보고서를 알림톡으로 전송, 프로모션 3일차 되는 계정(임시 계정||무료 계정)에 한해서 오후 12시에 학습 보고서를 알림톡으로 전송

## 프로젝트 형태
- Project: Maven Project
- Language: Java 17
- Spring Boot: 2.6.6
- Packaging: Jar
- Dependencies
    - Spring Web
    - MyBatis Framework
    - MySQL Driver
    - Lombok
    - Apache HttpClient (https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient, pom.xml 파일에 직접 추가)

## Update log
- 2022-06-09
  - logback
    - Spring boot에 내장된 logback을 활용하여 로그 저장
- 2022-07-13
  - 변경된 LMS에 맞춰서 알림톡 발송 로직 수정
  - 로그 내역에 찍힌 경고 메시지
    - WARN 22-07-13 12:00:00 [scheduling-1] [PoolBase-isConnectionAlive:184] - HikariPool-1 - Failed to validate connection com.mysql.cj.jdbc.ConnectionImpl@7868749e (No operations allowed after connection closed.). Possibly consider using a shorter maxLifetime value.
    - application-###-properties 파일에 hikari 설정 추가
      - mysql의 wait_timeout을 세션으로 설정하고 hikari의 max-lifetime을 mysql의 wait_timeout 보다 10초 적게 설정
- 2022-07-14
  - 학습 데이터 유무로 월간 보고서 발송 여부 로직
    - 현재
      - 매월 1일 전월의 월간 보고서 발송
    - 변경
      - 지난 1개월 차 학습 데이터가 없다면 지난 연속 2개월 학습 데이터가 없으면 월간 보고서가 발송되지 않는다는 내용을 알림톡에 추가
      - 지난 2개월 연속 학습 데이터가 없다면 알림톡 발송 X