package com.example.rental.model.car;

import com.example.rental.exceptions.CarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    @EventListener(ApplicationReadyEvent.class)
    public void dbInit() {

        jdbcTemplate.execute("DROP TABLE IF EXISTS " + "cars");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS cars(id BIGSERIAL PRIMARY KEY,name name, price decimal(18,2))");

        insertCar(new Car("Volvo S60", new BigDecimal(1500), true));
        insertCar(new Car("Volkswagen Golf", new BigDecimal(1333), true));
        insertCar(new Car("Ford Mustang", new BigDecimal(3000), true));
        insertCar(new Car("Ford Transit", new BigDecimal(2400), true));

        jdbcTemplate.execute("DROP TABLE IF EXISTS " + "rents");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS rents(id BIGSERIAL PRIMARY KEY,car_id int,driver_name name,driver_age int,start_time date, end_time date)");

    }
}
