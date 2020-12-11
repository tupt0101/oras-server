package capstone.oras.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static capstone.oras.common.Constant.JobStatus.*;

public class CommonUtils {
    @Autowired
    private static JavaMailSender javaMailSender;
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

    public static void sendMail(String email, String subject, String text) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject(subject);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        // use the true flag to indicate the text included is HTML
        helper.setText(text, true);
        javaMailSender.send(message);
    }
}
