package bln.sbre.client;

import bln.sbre.client.exc.SendRequestException;
import bln.sbre.client.query.QueryRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.List;

@Service
public class RestClient {
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    @Value( "${bems.server}" )
    private String baseUrl;

    @Value( "${bems.password}" )
    private String password;

    public ResponseEntity<String> request(List<QueryRequestDto> request) {

        //url
        String planUrl = "api/v1/external/plans/add-batch";
        String queryUrl = baseUrl + "/" + planUrl;

        //headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", "Basic " + password);

        //body
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String jsonStr;
        try {
            jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
        }
        catch (JsonProcessingException e) {
            throw new SendRequestException("Error during serializing query to json string");
        }

        //log request
        logger.trace("---url---");
        logger.trace(queryUrl);
        logger.trace("---headers---");
        logger.trace(headers.toString());
        logger.trace("");
        logger.trace("---body---");
        logger.trace(jsonStr);

        //send request
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        restTemplate.setErrorHandler(new MyErrorHandler());
        ResponseEntity<String> response = restTemplate.exchange(
            queryUrl,
            HttpMethod.POST,
            new HttpEntity<>(jsonStr, headers),
            String.class
        );

        //log response
        logger.trace("status: {}", response.getStatusCodeValue());
        logger.trace("response: {}", response.getBody());
        return response;
    }

    public <T> T jsonStringToObject(String jsonString, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(jsonString, clazz);
        }
        catch (IOException e) {
            throw new SendRequestException("Error during de-serializing json string to object");
        }
    }
}