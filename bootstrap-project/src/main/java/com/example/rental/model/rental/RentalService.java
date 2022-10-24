package com.example.rental.model.rental;


import com.example.rental.exceptions.AgeException;
import com.example.rental.exceptions.CarException;
import com.example.rental.exceptions.DateException;
import com.example.rental.model.car.Car;
import com.example.rental.model.car.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.datatype.Duration;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class RentalService {

    private final RentDataAccessService rentDataAccessService;
    private final CarService carService;

    public RentalService(RentDataAccessService rentDataAccessService, CarService carService) {
        this.rentDataAccessService = rentDataAccessService;
        this.carService = carService;
    }


    //Create RENT
    //special Exception TO DO
    public Rent createRental(final RentRequest rentRequest) {

        validateAge(rentRequest.getAgeDriver());
        startDataValidator(LocalDate.parse(rentRequest.getStart()));
        endDataValidator(LocalDate.parse(rentRequest.getStart()), LocalDate.parse(rentRequest.getEnd()));

        Car car = carService.getCarById(rentRequest.getCarId());

        if (car == null || rentDataAccessService.selectRentsByAvailableCar(car.getId(),
                LocalDate.parse(rentRequest.getStart()),
                LocalDate.parse(rentRequest.getEnd())) > 0)
        {
            throw new CarException("this car is not available at this time");
        }

        Rent rent = new Rent(car, rentRequest.getDriverName(),
                rentRequest.getAgeDriver(),
                LocalDate.parse(rentRequest.getStart()),
                LocalDate.parse(rentRequest.getEnd()));
        return rentDataAccessService.insertRent(rent);
    }

    // get all overviews for 2 view
    public List<OverviewRent> allOverviewRent() {
        List<Rent> rentList = getAllRent();
        List<OverviewRent> finalList = new ArrayList<>();
        for (int i = 0; i < rentList.size(); i++) {
            finalList.add(overviewRent(rentList.get(i)));
        }
        return finalList;
    }

    public BigDecimal summaryProfit() {
       return rentDataAccessService.sum();
    }

    public OverviewRent overviewRent(final Rent rent) {
        BigDecimal revenue = summaryCost(rent);
        return OverviewRent.builder()
                .nameOFDriver(rent.getDriverName())
                .car(rent.getCar())
                .fromTo(rent.getStartTime().toString() + " -> " + rent.getEndTime().toString())
                .revenue(revenue)
                .build();
    }

    public BigDecimal summaryCost(final Rent rent) {
        LocalDate date1 = rent.getStartTime();
        LocalDate date2 = rent.getEndTime();
        long daysDiff = ChronoUnit.DAYS.between(date1, date2);
        BigDecimal bigDecimal = new BigDecimal(daysDiff);
        return rent.getCar().getPrice().multiply(bigDecimal);
    }

    public List<Rent> getAllRent() {
        List<Rent> rents = rentDataAccessService.selectRentals();
        for (int i = 0; i < rents.size(); i++) {
            Rent rent = rents.get(i);
            rent.setCar(
                    carService.getCarById(rent.getCar().getId())
            );
        }
        return rents;
    }

    private void validateAge(final int age) {
        if (age < 18) {
            throw new AgeException("is under 18 years of age");
        }
    }

    private void startDataValidator(final LocalDate localDate) {
        if (localDate.isBefore(LocalDate.now())) {
            throw new DateException("future");
        }
    }

    private void endDataValidator(final LocalDate startDate, final LocalDate endDate) {
        if (endDate.isBefore(startDate) || (endDate == startDate)) {
            throw new DateException("end");
        }
    }

}
