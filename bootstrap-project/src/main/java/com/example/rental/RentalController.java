package com.example.rental;


import com.example.rental.model.car.Car;
import com.example.rental.model.car.CarService;
import com.example.rental.model.rental.dto.OverviewRent;
import com.example.rental.model.rental.Rent;
import com.example.rental.model.rental.dto.RentRequest;
import com.example.rental.model.rental.RentalService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "${allowed.origin}")
public class RentalController {

    private final CarService carService;
    private final RentalService rentalService;

    @GetMapping("/cars")
    public final List<Car> getCars() {
        return carService.getCars();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> addRent(@RequestBody final RentRequest body) {
        try {
            BigDecimal bigDecimal = rentalService.summaryCost(rentalService.createRental(body));
            return new ResponseEntity<>(bigDecimal, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/profit")
    public BigDecimal profit() {
        return rentalService.summaryProfit();
    }

    @GetMapping("/summary")
    public BigDecimal summaryCost(final Rent rent) {
        return rentalService.summaryCost(rent);
    }

    @GetMapping("/admin")
    public List<OverviewRent> getRent() {
        return rentalService.allOverviewRent();
    }
}
