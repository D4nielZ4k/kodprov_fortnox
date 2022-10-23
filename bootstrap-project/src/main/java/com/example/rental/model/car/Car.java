package com.example.rental.model.car;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class Car {

    private Integer id;
    private String name;
    private BigDecimal price;

    public Car(String name, BigDecimal price, boolean available) {
        this.name = name;
        this.price = price;
    }

}
