package ru.com.orderplatform.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderNotFoundExceptionTest {

    @Test
    void constructor_shouldKeepMessage() {
        // Arrange
        String message = "order-123";

        // Act
        OrderNotFoundException exception = new OrderNotFoundException(message);

        // Assert
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void constructor_shouldKeepCause() {
        // Arrange
        RuntimeException cause = new RuntimeException("db error");

        // Act
        OrderNotFoundException exception = new OrderNotFoundException(cause);

        // Assert
        assertThat(exception.getCause()).isSameAs(cause);
    }
}
