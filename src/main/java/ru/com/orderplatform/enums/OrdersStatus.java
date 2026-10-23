package ru.com.orderplatform.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrdersStatus {
    NEW("Новый заказ"),
    SEND("Заказ отправлен"),
    SUCCESS("Успешно");

    private final String description;
}
