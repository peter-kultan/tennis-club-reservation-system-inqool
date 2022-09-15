package cz.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.models.Court;
import cz.models.Surface;
import cz.payload.CourtPostRequest;
import cz.utils.TestDataGenerator;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CourtControllerTest {

    private final TestDataGenerator dataGenerator = new TestDataGenerator();
    private final String url = "/api/courts";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CourtController controller;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void createCourt() throws Exception {
        var court = dataGenerator.createTestCourt();
        saveSurface(court.getSurface());
        var result = mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createCourtPostRequest(court)))).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(mapper.readValue(result.getResponse().getContentAsString(), Court.class).getId()).isNotNull();
    }

    @Test
    public void getCourt() throws Exception {
        var court = dataGenerator.createTestCourt();
        saveSurface(court.getSurface());
        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createCourtPostRequest(court))));
        court.setId(1);

        var result = mvc.perform(MockMvcRequestBuilders.get(url + "/" + court.getId())).andReturn().getResponse();
        var sr = mapper.readValue(result.getContentAsString(), Court.class);
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(sr).isEqualTo(court);
    }

    @Test
    public void getCourts() throws Exception {
        var courts = dataGenerator.createTestCourts(3);
        for (int i = 0; i < courts.size(); i++) {
            saveSurface(courts.get(i).getSurface());
            mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createCourtPostRequest(courts.get(i)))));
            courts.get(i).setId(i + 1);
        }
        var result = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn().getResponse();
        var returnedCourts = mapper.readValue(result.getContentAsString(), Court[].class);
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(returnedCourts).containsAll(courts);
    }

    @Test
    public void updateCourt() throws Exception {
        var court = dataGenerator.createTestCourt();
        saveSurface(court.getSurface());
        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createCourtPostRequest(court))));
        court.setId(1);

        court.setHourPrice(2.25);
        var result = mvc.perform(MockMvcRequestBuilders.put(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(court))).andReturn().getResponse();

        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getContentAsString()).isEqualTo(mapper.writeValueAsString(court));
    }

    @Test
    public void deleteCourt() throws Exception {
        var court = dataGenerator.createTestCourt();
        saveSurface(court.getSurface());
        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(createCourtPostRequest(court))));
        court.setId(1);

        var result = mvc.perform(MockMvcRequestBuilders.delete(url + "/" + court.getId())).andReturn().getResponse();

        assertThat(result.getStatus()).isEqualTo(200);

        result = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn().getResponse();

        assertThat(result.getContentAsString()).doesNotContain(mapper.writeValueAsString(court));
    }

    private void saveSurface(Surface surface) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/surfaces").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(surface)));
        surface.setId(1);
    }

    private CourtPostRequest createCourtPostRequest(Court court) {
        return new CourtPostRequest(court.getSurface().getId(), court.getHourPrice());
    }
}
