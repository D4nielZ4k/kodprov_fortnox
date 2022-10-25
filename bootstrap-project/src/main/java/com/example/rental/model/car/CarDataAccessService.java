package com.example.rental.model.car;

import com.example.rental.exceptions.CarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class CarDataAccessService implements CarDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CarDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertCar(Car car) {
        var sql = """
                INSERT INTO cars(name, price)
                VALUES (?,?);
                 """;
        return jdbcTemplate.update(
                sql,
                car.getName(),
                car.getPrice()
        );
    }

    @Override
    public List<Car> selectCars() {
        var sql = """
                SELECT id, name, price
                FROM cars
                LIMIT 100;
                """;
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    //TO DO
    public List<Car> selectAccessibleCars() {
        var sql = """
                SELECT id, name, price
                FROM cars
                 """;
        return jdbcTemplate.query(sql, new CarRowMapper());
    }

    @Override
    public Car selectCarById(int id) {
        try {
            var sql = """
                    SELECT id, name, price
                    FROM cars
                    WHERE id = (?)
                     """;
            return jdbcTemplate.query(sql, new CarRowMapper(), id).stream()
                    .findFirst()
                    .orElseThrow();

        } catch (NoSuchElementException e) {
            throw new CarException("this car does not exist");
        }
    }

}
