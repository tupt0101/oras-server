package capstone.oras.common;

import capstone.oras.oauth2.controller.TokenDto;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static capstone.oras.common.Constant.JobStatus.*;
import static capstone.oras.common.Constant.OpenJobApi.OJ_LOGIN;

public class CommonUtils {
    private static String ojToken;

    public static String getOjToken() {
        if (StringUtils.isEmpty(ojToken)) {
            ojToken = getOpenJobToken();
        }
        return ojToken;
    }

    public static void setOjToken(String ojToken) {
        CommonUtils.ojToken = ojToken;
    }

    public static Date convertLocalDateTimeToDate(LocalDate localDate) {
        return Date.valueOf(localDate);
    }

    public static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert.toLocalDate());
    }

    public static LocalDateTime convertToLocalDateTimeViaSqlTimestamp(Date dateToConvert) {
        return new java.sql.Timestamp(
                dateToConvert.getTime()).toLocalDateTime();
    }

    public static RestTemplate initRestTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    public static HttpHeaders initHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ojToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public static <T1, T2> T2 handleOpenJobApi(String url, HttpMethod method, T1 requestBody, Class<T2> responseModel) {
        T2 ret = null;
        RestTemplate restTemplate = initRestTemplate();
        HttpHeaders httpHeaders = initHttpHeaders();
        HttpEntity<T1> entity = new HttpEntity<>(requestBody, httpHeaders);
        // request with retry
        for (int i = 0; i < 3; i++) {
            try {
                ret = restTemplate.exchange(url, method, entity, responseModel).getBody();
            } catch (HttpClientErrorException.Unauthorized e) {
                getOpenJobToken();
            } catch (Exception e) {
                System.out.println("ERROR AT handleOpenJobApi: " + e.getMessage());
            }
        }
        return ret;
    }

    public static Pageable configPageable(Integer numOfElement, Integer page, String sort) {
        String sortBy = sort.substring(1);
        return PageRequest.of(page != null ? page - 1 : 0, numOfElement != null ? numOfElement : Integer.MAX_VALUE,
                sort.startsWith("-") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
    }

    public static String jobActivityTitle(String title, String status) {
        String act = "";
        switch (status) {
            case DRAFT:
                act = "Draft a job with title \"" + title + "\"";
                break;
            case PUBLISHED:
                act = "Publish a job with title \"" + title + "\"";
                break;
            case CLOSED:
                act = "Close a job with title \"" + title + "\"";
                break;
        }
        return act;
    }

    public static String getOpenJobToken() {
        RestTemplate restTemplate = new RestTemplate();
        TokenDto dto;
        try {
            dto = restTemplate.getForObject(OJ_LOGIN, TokenDto.class);
            setOjToken(dto.getToken());
            return ojToken;
        } catch (Exception e) {
            return "";
        }
    }
}
