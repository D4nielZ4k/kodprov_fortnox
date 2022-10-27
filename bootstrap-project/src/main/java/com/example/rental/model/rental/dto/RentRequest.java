package com.example.rental.model.rental.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class RentRequest {

    private Integer carId;
    private String start;
    private String end;
    private String driverName;
    private int ageDriver;

}
