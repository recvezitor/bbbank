package com.dimas.bbbank;

import com.dimas.bbbank.api.ApiLoginRequest;
import com.dimas.bbbank.api.ApiLoginResponse;
import com.dimas.bbbank.api.ApiTransferRequest;
import com.dimas.bbbank.api.ApiTransferResponse;
import com.dimas.bbbank.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TransferIntegrationTest.Initializer.class)
@TestPropertySource(properties = {
        "spring.flyway.locations=classpath:/db/migration/,classpath:/db/after-migration/"
})
class TransferIntegrationTest {
    private static final String HOST = "localhost";
    @Container
    private static final PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:12")
            .withDatabaseName("postgres").withUsername("postgres").withPassword("postgres");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AccountService accountService;

    @AfterAll
    static void afterAll() {
        postgresContainer.stop();
    }


    @Test
    @Order(1)
    void ok_health() {
        ResponseEntity<String> response = health();
        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("UP"));
    }

    @Test
    void ok_login() {
        ApiLoginResponse auth = auth("1", "123");
        assertNotNull(auth);
        assertNotNull(auth.getAccessToken());
    }

    @Test
    void ok_transfer() {
        var accFromBefore = accountService.findById(1L);
        var balanceBefore = accFromBefore.getBalance();

        HttpEntity<ApiTransferRequest> entity = new HttpEntity<>(ApiTransferRequest.builder().amount(new BigDecimal(10)).recipientId(2L).build(), createAuthHeaders("1", "123"));
        var response = restTemplate.exchange(theUrl("/api/v1/users/transfer"), POST, entity, ApiTransferResponse.class);
        log.info("Transfer response={}", response);
        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());

        var accFromAfter = accountService.findById(1L);
        var balanceAfter = accFromAfter.getBalance();
        log.info("before={}, after={}", balanceBefore, balanceAfter);
        assertTrue(balanceAfter.compareTo(balanceBefore) < 0);
    }

    private ResponseEntity<String> health() {
        HttpEntity<String> entity = new HttpEntity<>(null, createHeaders());
        return restTemplate.exchange(theUrl("/actuator/health"), GET, entity, String.class);
    }


    private ApiLoginResponse auth(String login, String password) {
        HttpEntity<ApiLoginRequest> entity = new HttpEntity<>(ApiLoginRequest.builder().login(login).password(password).build(), createHeaders());
        var response = restTemplate.exchange(theUrl("/api/v1/auth/login"), POST, entity, ApiLoginResponse.class);
        log.info("Auth response={}", response);
        return response.getBody();
    }

    private HttpHeaders createAuthHeaders(String login, String password) {
        ApiLoginResponse auth = auth(login, password);
        var headers = createHeaders();
        headers.set("Authorization", "Bearer " + auth.getAccessToken());
        return headers;
    }

    private HttpHeaders createHeaders() {
        return new HttpHeaders() {{
            set("User-Agent", "rest-template");
            set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        }};
    }

    private String theUrl(String url) {
        return "http://" + HOST + ":" + port + url;
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            postgresContainer.setNetwork(Network.newNetwork());
            postgresContainer.setNetworkAliases(List.of("postgres-host"));
            postgresContainer.start();


            log.info("INITIALIZER");
            log.info("postgres: {}", postgresContainer.getJdbcUrl());

            TestPropertyValues values = TestPropertyValues.of(
                    "DB_HOST=" + postgresContainer.getHost(),
                    "DB_PORT=" + postgresContainer.getMappedPort(5432),
                    "DB_DATABASE=" + postgresContainer.getDatabaseName(),
                    "DB_SCHEMA=" + "bbbank"
            );
            values.applyTo(configurableApplicationContext);
        }

    }

}
