package com.walmart.coffee.squad.test.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDocument implements Serializable {
    /*
    Description: ResponseDocument is for multiple github events retrieved by singleId should be mapped to ResponseDocument
    TODO: Check ResponseDTO fields
     */
    private static final long serialVersionUID = 1L;

    String type;
    Long numDocuments;
    List<ESEntityDTO> data;
}
