package cz.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.models.Surface;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SurfaceControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SurfaceController controller;

    private TestDataGenerator dataGenerator;

    private final String url = "/api/surfaces";

    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        dataGenerator = new TestDataGenerator();
    }

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void createSurface() throws Exception {
        var surface = dataGenerator.createTestSurface();
        var result = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(surface))).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(surface.getId()).isNull();
    }

    @Test
    public void getSurface() throws Exception {
        var surface = dataGenerator.createTestSurface();
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(surface)));
        surface.setId(1);

        var result = mvc.perform(MockMvcRequestBuilders.get(url + "/" + surface.getId()))
                .andReturn().getResponse();
        var sr = mapper.readValue(result.getContentAsString(), Surface.class);
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(sr).isEqualTo(surface);
    }

    @Test
    public void getSurfaces() throws Exception {
        var surfaces = dataGenerator.createTestSurfaces(3);
        for (int i = 0; i < surfaces.size(); i++) {
            mvc.perform(MockMvcRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(surfaces.get(i))));
            surfaces.get(i).setId(i + 1);
        }
        var result = mvc.perform(MockMvcRequestBuilders.get(url))
                .andReturn().getResponse();
        var surs = mapper.readValue(result.getContentAsString(), Surface[].class);
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(surfaces).containsExactly(surs);
    }

    @Test
    public void updateSurface() throws Exception {
        var surface = dataGenerator.createTestSurface();
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(surface)));
        surface.setId(1);

        surface.setName("newName");
        var result = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(surface)))
                .andReturn().getResponse();

        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getContentAsString()).isEqualTo(mapper.writeValueAsString(surface));
    }

    @Test
    public void deleteSurface() throws Exception {
        var surface = dataGenerator.createTestSurface();
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(surface)));
        surface.setId(1);

        var result = mvc.perform(MockMvcRequestBuilders.delete(url + "/" + surface.getId()))
                .andReturn().getResponse();

        assertThat(result.getStatus()).isEqualTo(200);

        result = mvc.perform(MockMvcRequestBuilders.get(url))
                .andReturn().getResponse();

        assertThat(result.getContentAsString()).doesNotContain(mapper.writeValueAsString(surface));
    }
}
