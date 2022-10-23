package com.example.rental.model.rental;


import com.example.rental.model.car.Car;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@EqualsAndHashCode
public class Rent {

    private Integer id;

    private Car car;

    private String driverName;

    private int driverAge;

    private LocalDate startTime;

    private LocalDate endTime;

    public Rent(Car car, String driverName, int driverAge, LocalDate startTime, LocalDate endTime) {
        this.car = car;
        this.driverName = driverName;
        this.driverAge = driverAge;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
