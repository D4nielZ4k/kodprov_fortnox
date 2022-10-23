package com.example.rental.model.car;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarDataAccessService dataAccessService;

    public void addCar(final Car car) {
        dataAccessService.insertCar(car);
    }

    public List<Car> getCars() {
        return dataAccessService.selectCars();
    }

    public List<Car> getAvailableCars() {
        return dataAccessService.selectAccessibleCars();
    }

    public Car getCarById(final Integer id) {
        return dataAccessService.selectCarById(id);
    }


}
