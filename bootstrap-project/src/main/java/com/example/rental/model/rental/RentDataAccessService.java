package com.example.rental.model.rental;

import com.example.rental.exceptions.CarException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository
public class RentDataAccessService implements RentDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RentDataAccessService(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Rent insertRent(Rent rent) {

        if (selectRentsByAvailableCar(rent.getCar().getId(), rent.getStartTime(), rent.getEndTime()) > 0) {
            throw new CarException("this car is not available at this time");
        }
        var sql = """
                INSERT INTO rents(car_id, driver_name,driver_age,start_time,end_time)
                VALUES (?,?,?,?,?)
                 """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, rent.getCar().getId());
            ps.setString(2, rent.getDriverName());
            ps.setInt(3, rent.getDriverAge());
            ps.setDate(4, Date.valueOf(rent.getStartTime()));
            ps.setDate(5, Date.valueOf(rent.getEndTime()));
            return ps;
        }, keyHolder);

        rent.setId(keyHolder.getKey().intValue());
        return rent;
    }

    public int selectRentsByAvailableCar(final int carId, final LocalDate startTime, final LocalDate endTime) {
        String sql = "SELECT * from rents where car_id=" + carId +
                " and start_time between '" + startTime + "' and '" + endTime +
                "' or end_time  between '" + endTime + "' and '" + startTime + "'";

        return jdbcTemplate.query(sql, new RentalRowMapper()).size();
    }

    @Override
    public List<Rent> selectRentals() {
        var sql = """
                SELECT id,car_id, driver_name,driver_age,start_time,end_time
                FROM rents
                LIMIT 100;
                 """;
        return jdbcTemplate.query(sql, new RentalRowMapper());
    }

    public BigDecimal sum(){
        String sql = "SELECT sum(c.price*(r.end_time-r.start_time))" +
                "from rents r left join cars c on c.id = R.car_id;";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class);
    }
}
