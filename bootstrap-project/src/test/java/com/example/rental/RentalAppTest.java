package com.example.rental;


import com.example.rental.exceptions.AgeException;
import com.example.rental.exceptions.CarException;
import com.example.rental.exceptions.DateException;
import com.example.rental.model.car.Car;
import com.example.rental.model.car.CarDataAccessService;
import com.example.rental.model.car.CarService;
import com.example.rental.model.rental.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest()
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RentalAppTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RentalService rentalService;

    @InjectMocks
    RentalService service;

    @MockBean
    CarDataAccessService carDataAccessService;

    @Mock
    RentDataAccessService rentDataAccessService;

    @Mock
    CarService carService;


    @Test
    public void shouldReturnListOfOverviewRents() throws Exception {
        // given
        Car car = new Car(1, "car1", new BigDecimal(1200.00));
        Car car2 = new Car(2, "car2", new BigDecimal(1200.00));
        OverviewRent overviewRent = OverviewRent.builder()
                .nameOFDriver("uno")
                .car(car)
                .fromTo("from to ")
                .revenue(new BigDecimal(12))
                .build();
        OverviewRent overviewRent2 = OverviewRent.builder()
                .nameOFDriver("duo")
                .car(car2)
                .fromTo("from to ")
                .revenue(new BigDecimal(1000))
                .build();

        List<OverviewRent> overviewRentList = new ArrayList<>();
        overviewRentList.add(overviewRent);
        overviewRentList.add(overviewRent2);

        given(rentalService.allOverviewRent()).willReturn(overviewRentList);

        // when
        ResultActions response = mockMvc.perform(get("/api/admin"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(overviewRentList.size())));

    }

    @Test
    public void findCarById() throws Exception {
        // given
        Car car = new Car(1, "car1", new BigDecimal(1200.00));
        given(carService.getCarById(1)).willReturn(car);

        // when
        Car finalCar = carService.getCarById(1);

        // then
        assertThat(finalCar).isNotNull();
        assertThat(finalCar.getId().equals(1));
        assertThat(finalCar.getName().equals("car1"));
    }

    @Test
    public void ShouldSumProfit() throws Exception {

        // then
        when(rentalService.summaryProfit()).thenReturn(new BigDecimal(5000));
        ResultActions response = mockMvc.perform(get("/api/profit"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(result -> result.equals(5000));

    }


    @Test
    public void shouldThrowCarException() {

        // given
        Car car = new Car(1, "car1", new BigDecimal(1200.00));
        RentRequest rentRequest = RentRequest.builder()
                .carId(1)
                .start("2023-12-11")
                .end("2023-12-30")
                .ageDriver(18)
                .driverName("name")
                .build();
        Rent rent = Rent.builder()
                .id(1)
                .car(car)
                .driverName("name")
                .driverAge(22)
                .startTime(LocalDate.parse("2023-12-11"))
                .endTime(LocalDate.parse("2023-12-30"))
                .build();
        given(rentDataAccessService.selectRentals()).willReturn(List.of(rent));

        // then
        assertThatThrownBy(() -> service.createRental(rentRequest)).isInstanceOf(CarException.class);

    }

    @Test
    public void ShouldThrowDateEx() {
        // given
        RentRequest rentRequest = RentRequest.builder()
                .carId(1)
                .start("2011-12-11") //Date before now()
                .end("2023-12-30")
                .ageDriver(22)
                .driverName("name")
                .build();
        // then
        assertThatThrownBy(() -> service.createRental(rentRequest)).isInstanceOf(DateException.class);

    }

    @Test
    public void shouldThrowAgeException() {
        // given
        RentRequest rentRequest = RentRequest.builder()
                .carId(1)
                .start("2023-12-11")
                .end("2023-12-30")
                .ageDriver(10) //age is under 18
                .driverName("name")
                .build();
        // then
        assertThatThrownBy(() -> service.createRental(rentRequest)).isInstanceOf(AgeException.class);

    }


    @Test
    public void shouldReturnRentsList() {
        // given
        Car car = new Car(1, "car1", new BigDecimal(1200.00));
        Rent rent = Rent.builder()
                .id(1)
                .car(car)
                .driverName("name")
                .driverAge(22)
                .startTime(LocalDate.parse("2023-12-11"))
                .endTime(LocalDate.parse("2023-12-30"))
                .build();
        Rent rent1 = Rent.builder()
                .id(1)
                .car(car)
                .driverName("name")
                .driverAge(22)
                .startTime(LocalDate.parse("2023-12-11"))
                .endTime(LocalDate.parse("2023-12-30"))
                .build();

        given(rentDataAccessService.selectRentals()).willReturn(List.of(rent, rent1));

        // when
        List<Rent> ret = service.getAllRent();

        // then
        assertThat(ret).isNotNull();
        assertThat(ret.size()).isEqualTo(2);
    }

}
