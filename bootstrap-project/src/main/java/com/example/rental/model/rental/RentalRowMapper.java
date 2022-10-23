package com.example.rental.model.rental;

import com.example.rental.model.car.Car;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class RentalRowMapper implements RowMapper<Rent> {

    public Rent mapRow(ResultSet rs, int rowNum) throws SQLException {
        Car car = Car.builder()
                .id(rs.getInt("car_id"))
                .build();
        return new Rent(
                rs.getInt("id"),
                car,
                rs.getString("driver_name"),
                rs.getInt("driver_age"),
                rs.getDate("start_time").toLocalDate(),
                rs.getDate("end_time").toLocalDate()
        );
    }

}
