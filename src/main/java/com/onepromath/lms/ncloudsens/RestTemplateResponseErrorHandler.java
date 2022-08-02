package com.onepromath.lms.ncloudsens;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/**
 * @author Lee Taesung
 * @since 2022/08/01
 */

@Component
@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler { // RestTemplate 예외 처리 재 구현

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

        return (
                httpResponse.getStatusCode().series() == CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        String seriesName = "UNKNOWN_ERROR";

        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            // handle SERVER_ERROR
            seriesName = SERVER_ERROR.name();
        } else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            // handle CLIENT_ERROR
            seriesName = CLIENT_ERROR.name();
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                seriesName = HttpStatus.NOT_FOUND.name();
            }
        }

        log.info("REST TEMPLATE ERROR ======");
        log.info("SERIES_NAME: {}", seriesName);
        log.info("STATUS_CODE: {}", httpResponse.getStatusCode());
        log.info("REST TEMPLATE ERROR ======");
    }
}
