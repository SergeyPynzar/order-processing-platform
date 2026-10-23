package ru.com.orderplatform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import ru.com.orderplatform.model.OrdersEntity;
import ru.com.orderplatform.repository.OrdersRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class OrdersFlowIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrdersRepository ordersRepository;

    @Test
    void createOrder_thenGetOrder_shouldPersistSameData() throws Exception {
        // Arrange
        String requestBody = """
                {
                  "name": "Laptop",
                  "quantity": 2,
                  "price": 999.99,
                  "status": "NEW"
                }
                """;

        // Act
        MvcResult createResult = mockMvc.perform(post("/v1/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        UUID orderId = UUID.fromString(createResult.getResponse().getContentAsString().replace("\"", ""));

        mockMvc.perform(get("/v1/api/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId.toString()))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.price").value(999.99))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.created").isNotEmpty());

        // Assert
        OrdersEntity saved = ordersRepository.findById(orderId).orElseThrow();
        assertThat(saved.getName()).isEqualTo("Laptop");
        assertThat(saved.getQuantity()).isEqualTo(2);
        assertThat(saved.getPrice()).isEqualByComparingTo(new BigDecimal("999.99"));
        assertThat(saved.getStatus().name()).isEqualTo("NEW");
        assertThat(saved.getCreated()).isNotNull();
    }
}
