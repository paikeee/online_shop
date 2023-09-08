package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.models.Order;
import ru.nicetu.online_shop.models.OrderProduct;

import java.util.List;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        emailSender.send(simpleMailMessage);
    }

    @Override
    public String buildPurchaseMessage(Order order) {
        StringBuilder products = new StringBuilder();
        List<OrderProduct> productList = order.getOrderProducts();
        productList.forEach( it -> products.append(it.toString()).append("\n"));
        return "Hello, " + order.getPerson().username() + "!\n" +
                "Your order №" + order.getOrderId() + " has been successfully placed.\n" +
                "Your order:\n" +
                products +
                "Total: " + productList.stream().mapToInt(OrderProduct::getTotalPrice).sum() + " RUB";
    }
}
