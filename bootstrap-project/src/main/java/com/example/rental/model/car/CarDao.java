package com.example.rental.model.car;

import java.util.List;

public interface CarDao {

    List<Car> selectCars();
    int insertCar(Car car);
    Car selectCarById(int id);
}
