package ru.nicetu.online_shop.services;

import ru.nicetu.online_shop.models.Order;

public interface EmailService {

    void sendEmail(String to, String subject, String message);

}
