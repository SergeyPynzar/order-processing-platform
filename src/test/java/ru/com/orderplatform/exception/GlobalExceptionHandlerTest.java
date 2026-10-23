package ru.com.orderplatform.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void orderNotFound_shouldReturnNotFoundWithMessage() {
        // Arrange
        OrderNotFoundException exception = new OrderNotFoundException("missing-id");

        // Act
        ResponseEntity<String> response = handler.orderNotFound(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Order not found.");
    }
}
