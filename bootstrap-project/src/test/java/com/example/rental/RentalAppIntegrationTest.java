
package com.example.rental;

import com.example.rental.exceptions.AgeException;
import com.example.rental.exceptions.CarException;
import com.example.rental.exceptions.NameException;
import com.example.rental.model.car.CarService;
import com.example.rental.model.rental.dto.OverviewRent;
import com.example.rental.model.rental.dto.RentRequest;
import com.example.rental.model.rental.RentalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import org.hamcrest.Matchers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RentalAppIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarService carService;

    @Autowired
    private RentalService rentalService;

   @BeforeEach
   void before() {
       jdbcTemplate.execute("DELETE FROM rents");
   }

    @Test
    void shouldReturnCarsList() throws Exception {
        System.out.println(carService.getCars().size());
        this.mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

    }

    @Test
    void shouldAddRent() throws Exception {
        // given
        RentRequest rentRequest = RentRequest.builder()
                .carId(1)
                .start("2023-12-11")
                .end("2023-12-30")
                .ageDriver(18)
                .driverName("name")
                .build();
        // when
        rentalService.createRental(rentRequest);
        // then

        Assertions.assertEquals(1, rentalService.getAllRent().size());
    }

    @Test
    void shouldNotAddRentAndThrowAgeException() {
        // given
        RentRequest rentRequest = RentRequest.builder()
                .carId(1)
                .start("2023-12-11")
                .end("2023-12-30")
                .ageDriver(11)
                .driverName("name")
                .build();
        // when
        // then
        assertThatThrownBy(() -> rentalService.createRental(rentRequest)).isInstanceOf(AgeException.class);
        Assertions.assertEquals(0, rentalService.getAllRent().size());
    }

    @Test
    void shouldNotAddRentAndThrowDateException() {
        // given
        RentRequest rentRequest = RentRequest.builder()
                .carId(1)
                .start("2023-12-11")
                .end("2023-12-30")
                .ageDriver(18)
                .driverName("name")
                .build();

        RentRequest rentRequest2 = RentRequest.builder()
                .carId(1)
                .start("2023-12-11")
                .end("2023-12-30")
                .ageDriver(18)
                .driverName("name")
                .build();
        // when
        rentalService.createRental(rentRequest2);
        // then
        assertThatThrownBy(() -> rentalService.createRental(rentRequest)).isInstanceOf(CarException.class);
        Assertions.assertEquals(1, rentalService.getAllRent().size());
    }


    @Test
    void shouldNotAddRentAndThrowNameException() {
        // given
        RentRequest rentRequest = RentRequest.builder()
                .carId(1)
                .start("2023-12-11")
                .end("2023-12-30")
                .ageDriver(18)
                .driverName("name")
                .build();

        RentRequest rentRequest2 = RentRequest.builder()
                .carId(1)
                .start("2023-12-11")
                .end("2023-12-30")
                .ageDriver(18)
                .driverName("na2me32")
                .build();
        // when
        rentalService.createRental(rentRequest);
        // then
        assertThatThrownBy(() -> rentalService.createRental(rentRequest2)).isInstanceOf(NameException.class);
        Assertions.assertEquals(1, rentalService.getAllRent().size());
    }

    @Test
    void shouldReturnProfit() {
        // given
        RentRequest rentRequest = RentRequest.builder()
                .carId(3)
                .start("2023-12-11")
                .end("2023-12-12")
                .ageDriver(18)
                .driverName("name")
                .build();

        RentRequest rentRequest2 = RentRequest.builder()
                .carId(1)
                .start("2023-12-11")
                .end("2023-12-12")
                .ageDriver(18)
                .driverName("name")
                .build();
        rentalService.createRental(rentRequest2);
        rentalService.createRental(rentRequest);
        // when
        BigDecimal incoming = new BigDecimal(String.valueOf(rentalService.summaryProfit()));
        BigDecimal expected = new BigDecimal("4500.00");
        // then

        assertThat(incoming, Matchers.comparesEqualTo(expected));
    }


    @Test
    void shouldReturnOverviewRents(){
        // given
        RentRequest rentRequest = RentRequest.builder()
                .carId(3)
                .start("2023-12-11")
                .end("2023-12-13")
                .ageDriver(18)
                .driverName("testName")
                .build();
        rentalService.createRental(rentRequest);
        // when

        List<OverviewRent> overviewRentList = rentalService.allOverviewRent();
        // then

        Assertions.assertEquals(1, rentalService.getAllRent().size());
        Assertions.assertEquals(overviewRentList.get(0).getRevenue(), new BigDecimal("6000.00"));
        Assertions.assertTrue(overviewRentList.get(0).getNameOFDriver().equals("testName"));
        Assertions.assertTrue(overviewRentList.get(0).getCar().getName().equals("Ford Mustang"));
    }

}
