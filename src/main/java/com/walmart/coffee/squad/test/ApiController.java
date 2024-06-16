package com.walmart.coffee.squad.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
public class ApiController {


    @PostMapping(value = "/v1/data", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addPickupPointService(@RequestBody Object requestData) {
        System.out.println(requestData.toString());
        return ResponseEntity.ok(requestData);
    }
}
