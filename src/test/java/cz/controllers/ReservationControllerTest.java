package cz.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.enums.ReservationType;
import cz.models.Court;
import cz.models.Reservation;
import cz.models.Surface;
import cz.payload.CourtPostRequest;
import cz.payload.ReservationPostRequest;
import cz.repositories.CourtRepository;
import cz.repositories.UserRepository;
import cz.utils.TestDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    private final TestDataGenerator dataGenerator = new TestDataGenerator();
    private final String url = "/api/reservations";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ReservationController controller;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourtRepository courtRepository;

    @Before
    public void init() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void createReservation() throws Exception {
        var reservation = dataGenerator.createTestReservation();
        reservation.setCourt(saveCourt(reservation.getCourt()));
        var reservationRequest = createReservationPostRequest(reservation);

        var response = mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reservationRequest))).andReturn().getResponse();

        reservation.setId(1);
        assertThat(response.getStatus()).isEqualTo(201);

        var price = reservation.getDuration().toHours() * reservation.getCourt().getHourPrice() * (reservation.getReservationType() == ReservationType.Doubles ? 1.5 : 1);
        assertThat(mapper.readValue(response.getContentAsString(), Double.class)).isEqualTo(price);
    }

    @Test
    public void getReservation() throws Exception {
        var reservation = dataGenerator.createTestReservation();
        reservation.setCourt(saveCourt(reservation.getCourt()));
        var reservationRequest = createReservationPostRequest(reservation);

        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reservationRequest))).andReturn().getResponse();

        var response = mvc.perform(MockMvcRequestBuilders.get(url + "/1")).andReturn().getResponse();

        reservation.getUser().setId(1);
        reservation.setId(1);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(mapper.readValue(response.getContentAsString(), Reservation.class)).isEqualTo(reservation);
    }

    @Test
    public void getReservations() throws Exception {
        var reservations = dataGenerator.createTestReservations(3);

        reservations.get(0).setStartDateTime(LocalDateTime.now().plus(4, ChronoUnit.YEARS));
        reservations.get(1).setStartDateTime(LocalDateTime.now().plus(6, ChronoUnit.YEARS));
        reservations.get(2).setStartDateTime(LocalDateTime.now().plus(8, ChronoUnit.YEARS));

        for (int i = 0; i < 3; i++) {
            reservations.get(i).setCourt(saveCourt(reservations.get(i).getCourt()));

            var reservationRequest = createReservationPostRequest(reservations.get(i));

            mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reservationRequest)));

            reservations.get(i).getUser().setId(i + 1);

            reservations.get(i).setId(i + 1);
        }

        var response = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn().getResponse();

        assertThat(mapper.readValue(response.getContentAsString(), Reservation[].class)).containsAll(reservations);
    }

    @Test
    public void UpdateReservation() throws Exception {
        var reservation = dataGenerator.createTestReservation();

        reservation.setCourt(saveCourt(reservation.getCourt()));

        var reservationRequest = createReservationPostRequest(reservation);

        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reservationRequest)));

        reservation.getUser().setId(1);

        reservation.setStartDateTime(LocalDateTime.now().plus(15, ChronoUnit.YEARS));
        reservation.setDuration(Duration.ofHours(15));

        reservation.setId(1);

        var response = mvc.perform(MockMvcRequestBuilders.put(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reservation))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        response = mvc.perform(MockMvcRequestBuilders.get(url + "/" + reservation.getId())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(mapper.readValue(response.getContentAsString(), Reservation.class)).isEqualTo(reservation);
    }

    @Test
    public void deleteReservation() throws Exception {
        var reservation = dataGenerator.createTestReservation();

        reservation.setCourt(saveCourt(reservation.getCourt()));

        var reservationRequest = createReservationPostRequest(reservation);

        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reservationRequest)));

        reservation.getUser().setId(1);

        reservation.setId(1);

        var response = mvc.perform(MockMvcRequestBuilders.delete(url + "/" + reservation.getId())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        response = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(mapper.readValue(response.getContentAsString(), Reservation[].class)).doesNotContain(reservation);

        assertThat(userRepository.findAllUsers()).contains(reservation.getUser());
        assertThat(courtRepository.findAllCourts()).contains(reservation.getCourt());
    }

    private Court saveCourt(Court court) throws Exception {
        var response = mvc.perform(MockMvcRequestBuilders.post("/api/surfaces").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(court.getSurface()))).andReturn().getResponse();

        court.setSurface(mapper.readValue(response.getContentAsString(), Surface.class));

        var courtPostRequest = new CourtPostRequest(court.getSurface().getId(), court.getHourPrice());

        response = mvc.perform(MockMvcRequestBuilders.post("/api/courts").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(courtPostRequest))).andReturn().getResponse();

        return mapper.readValue(response.getContentAsString(), Court.class);
    }

    private ReservationPostRequest createReservationPostRequest(Reservation reservation) {
        return new ReservationPostRequest(reservation.getCourt().getId(), reservation.getUser().getPhoneNumber(), reservation.getUser().getName(), reservation.getStartDateTime().toString(), reservation.getDuration().toMinutes(), reservation.getReservationType());
    }
}
