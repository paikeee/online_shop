package ru.nicetu.online_shop.services;

public interface EmailService {

    void sendEmail(String to, String subject, String message);

}
