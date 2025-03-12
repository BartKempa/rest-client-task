package com.example.rest_client_task.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class GitHubControllerTest {


    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/repositories/";

    private WireMockServer wireMockServer;

    @BeforeAll
    void startWireMock() {
        wireMockServer = new WireMockServer(9561);
        wireMockServer.start();
        configureFor("localhost", 9561);
    }

    @AfterAll
    void stopWireMock() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setupMockResponses() {
        wireMockServer.resetAll();

        String githubResponse = """
            [
                {
                    "name": "my-repo",
                    "owner": { "login": "bartkempa" },
                    "fork": false
                }
            ]
        """;

        wireMockServer.stubFor(get(urlEqualTo("/users/bartkempa/repos"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(githubResponse)));

        String branchesResponse = """
            [
                {
                    "name": "main",
                    "commit": { "sha": "abc123" }
                }
            ]
        """;

        wireMockServer.stubFor(get(urlEqualTo("/repos/bartkempa/my-repo/branches"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(branchesResponse)));
    }


    @Test
    void shouldReturnRepositoriesForUser() throws Exception {
        // given
        String username = "bartkempa";

        // when & then
        mockMvc.perform(get(BASE_URL + "{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].repositoryName").value("my-repo"))
                .andExpect(jsonPath("$[0].ownerLogin").value("bartkempa"))
                .andExpect(jsonPath("$[0].branches[0].branchName").value("main"))
                .andExpect(jsonPath("$[0].branches[0].commitSha").value("abc123"));
    }

}
