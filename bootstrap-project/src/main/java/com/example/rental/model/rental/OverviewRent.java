package com.example.rental.model.rental;

import com.example.rental.model.car.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OverviewRent {

    private String nameOFDriver;
    private Car car;
    private String fromTo;
    private BigDecimal revenue;

}
