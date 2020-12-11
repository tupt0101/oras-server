package capstone.oras.common;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonUtils {
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

    public static RestTemplate initRestTemplate(){
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    public static Pageable configPageable(Integer numOfElement, Integer page, String sort) {
        String sortBy = sort.substring(1);
        return PageRequest.of(page != null ? page - 1 : 0, numOfElement != null ? numOfElement : Integer.MAX_VALUE,
                sort.startsWith("-") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
    }
}
