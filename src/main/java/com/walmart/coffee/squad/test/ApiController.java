package com.walmart.coffee.squad.test;

import java.time.ZonedDateTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.walmart.coffee.squad.test.dto.ESEntityDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


@RestController
@Slf4j
public class ApiController {


    @Autowired
    ESIndexDao esIndexDao;

    @PostMapping(value = "/v1/data", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getGitHubWebhooks(@RequestBody ESEntityDTO requestData) throws JsonProcessingException {
        esIndexDao.publishDataToES(requestData, generateESIndexName());
        return ResponseEntity.ok(requestData);
    }

    public String generateESIndexName() {
        ZonedDateTime currTime = ZonedDateTime.now();
        int currMonth = currTime.getMonthValue();
        int currYear =  currTime.getYear();
        int currDate =  currTime.getDayOfMonth();
        return "github_" + currDate + "_" + currMonth + "_" + currYear;
    }
}
