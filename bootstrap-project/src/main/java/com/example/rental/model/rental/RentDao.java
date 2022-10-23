package com.example.rental.model.rental;


import java.util.List;

public interface RentDao {
    Rent insertRent(Rent rent);
    List<Rent> selectRentals();
}
