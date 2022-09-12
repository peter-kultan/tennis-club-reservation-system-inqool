package cz.utils;

import cz.enums.ReservationType;
import cz.models.Court;
import cz.models.Reservation;
import cz.models.Surface;
import cz.models.User;


import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataGenerator {

    private final List<String> surfaceNames =
            List.of("dirt", "grass", "sand", "gravel", "asphalt", "clay");

    private final List<List<String>> user =
            List.of(
                    List.of(
                                    "+421852475324",
                                    "+420855741963",
                                    "+987521425368",
                                    "+521785365854",
                                    "0987584214",
                                    "9824524857",
                                    "9876543210",
                                    "0123456789"
                            ),
                            List.of("Clark Dyer",
                                    "Sultan Moreno",
                                    "Mia Wallace",
                                    "Simona Suarez",
                                    "Jaeden Page",
                                    "Saniya Puckett",
                                    "Kayley Duke",
                                    "Rihanna Dawe",
                                    "Dakota Castro",
                                    "Cem Crossley")
            );

    private final Random random = new Random(654169874153241L);

    public Surface createTestSurface() {
        String name = selectRandom(surfaceNames);
        return new Surface(name);
    }

    public List<Surface> createTestSurfaces(int count) {
        return Stream.generate(this::createTestSurface).limit(count).collect(Collectors.toList());
    }

    public Court createTestCourt() {
        var surface = createTestSurface();
        double price = random.nextDouble(20, 80);
        return new Court(surface, price);
    }

    public List<Court> createTestCourts(int count) {
        return Stream.generate(this::createTestCourt).limit(count).collect(Collectors.toList());
    }

    public User createTestUser() {
        var phoneNumber = selectRandom(user.get(0));
        var name = selectRandom(user.get(1));
        return new User(phoneNumber, name);
    }

    public List<User> createTestUsers(int count) {
        return Stream.generate(this::createTestUser).limit(count).collect(Collectors.toList());
    }

    public Reservation createTestReservation() {
        var court = createTestCourt();
        var user = createTestUser();

        long minDay = LocalDate.now().toEpochDay();
        long maxDay = LocalDate.of(2060, 12, 31).toEpochDay();
        long randomDay = random.nextLong(minDay, maxDay);
        var startDateTime = LocalDateTime.of(LocalDate.ofEpochDay(randomDay),
                LocalTime.of(random.nextInt(0, 24), 0));

        var duration = Duration.of(random.nextInt(30, 300), ChronoUnit.HOURS);
        var reservationType = selectRandom(Arrays.asList(ReservationType.values()));
        return new Reservation(court, user, startDateTime, duration, reservationType);
    }

    public List<Reservation> createTestReservations(int count) {
        return Stream.generate(this::createTestReservation).limit(count).collect(Collectors.toList());
    }

    private <T> T selectRandom(List<T> data) {
        return data.get(random.nextInt(data.size()));
    }

}
