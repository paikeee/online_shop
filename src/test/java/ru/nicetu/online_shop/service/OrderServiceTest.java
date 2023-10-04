package ru.nicetu.online_shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nicetu.online_shop.dto.request.OrderProductRequest;
import ru.nicetu.online_shop.dto.request.OrderRequest;
import ru.nicetu.online_shop.dto.response.OrderDTO;
import ru.nicetu.online_shop.models.Order;
import ru.nicetu.online_shop.models.OrderProduct;
import ru.nicetu.online_shop.models.Person;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.repository.OrderRepository;
import ru.nicetu.online_shop.services.EmailServiceImpl;
import ru.nicetu.online_shop.services.OrderServiceImpl;
import ru.nicetu.online_shop.services.PersonDetailsService;
import ru.nicetu.online_shop.services.ProductServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private PersonDetailsService personDetailsService;

    @Mock
    private EmailServiceImpl emailService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void testGetOrder() {
        Order expected = new Order();
        expected.setOrderId(1);
        when(orderRepository.findByOrderId(1)).thenReturn(Optional.of(expected));
        Order result = orderService.getOrder(1);
        assertEquals(expected, result);
        assertThrows(NoSuchElementException.class, () -> orderService.getOrder(0));
    }

    @Test
    public void testSave() {
        Order order = new Order();
        orderService.save(order);
        verify(orderRepository).save(order);
    }

    @Test
    public void testGetTotalPrice() {
        List<OrderProduct> orderProducts = new ArrayList<>();
        Product product1 = new Product();
        Product product2 = new Product();
        product1.setPrice(100);
        product1.setDiscount(20);
        product2.setPrice(10);
        product2.setDiscount(0);
        orderProducts.add(new OrderProduct(null, product1, 1));
        orderProducts.add(new OrderProduct(null, product2, 2));
        when(productService.getActualPrice(product1)).thenReturn(80);
        when(productService.getActualPrice(product2)).thenReturn(10);
        assertEquals(100, orderService.getTotalPrice(orderProducts));
    }

    @Test
    public void testDeleteSelectedProducts() {
        List<OrderProduct> orderProducts = new ArrayList<>();
        Product product1 = new Product();
        Product product2 = new Product();
        product1.setAmount(10);
        product1.setProductId(1);
        product2.setAmount(10);
        orderProducts.add(new OrderProduct(null, product1, 1));
        orderProducts.add(new OrderProduct(null, product2, 2));

        orderService.deleteSelectedProducts(orderProducts);
        verify(productService, times(2)).save(any());
        assertEquals(9, product1.getAmount());
        assertEquals(8, product2.getAmount());
        product1.setAmount(0);
        assertThrows(NoSuchElementException.class, () -> orderService.deleteSelectedProducts(orderProducts));
    }

    @Test
    public void testCheckSelectedProducts() {
        List<OrderProduct> orderProducts = new ArrayList<>();
        Product product1 = new Product();
        product1.setAmount(0);
        orderProducts.add(new OrderProduct(null, product1, 1));
        assertThrows(NoSuchElementException.class ,() -> orderService.checkSelectedProducts(orderProducts));
        product1.setAmount(10);
        assertDoesNotThrow(() -> orderService.checkSelectedProducts(orderProducts));
    }

    @Test
    public void testNewOrder() {
        List<OrderProductRequest> list = new ArrayList<>();
        list.add(new OrderProductRequest(1, 2));
        list.add(new OrderProductRequest(2, 1));
        OrderRequest request = new OrderRequest("email", list);
        Product product1 = new Product();
        product1.setProductId(1);
        product1.setPrice(100);
        product1.setAmount(10);
        Product product2 = new Product();
        product2.setProductId(2);
        product2.setPrice(200);
        product2.setAmount(10);

        when(productService.getProduct(1)).thenReturn(product1);
        when(productService.getProduct(2)).thenReturn(product2);
        when(productService.getActualPrice(product1)).thenReturn(100);
        when(productService.getActualPrice(product2)).thenReturn(200);
        OrderDTO result = orderService.newOrder(request);
        assertEquals(0, result.getOrderId());
        assertEquals(400, result.getTotalPrice());
        assertEquals(LocalDate.now(), result.getDate());
        verify(personDetailsService).findByEmail(request.getEmail());
    }

    @Test
    public void testMakePurchase() {
        Person person = new Person();
        person.setName("Sergei");
        person.setSurname("Leonov");
        person.setEmail("email");
        Product product1 = new Product("name1", "", 100, 10, 10);
        Product product2 = new Product("name2", "", 200, 10, 0);
        product1.setProductId(1);
        product2.setProductId(2);
        List<OrderProduct> list = new ArrayList<>();
        OrderProduct orderProduct1 = new OrderProduct(null, product1, 2);
        OrderProduct orderProduct2 = new OrderProduct(null, product2, 1);
        list.add(orderProduct1);
        list.add(orderProduct2);
        Order order = new Order(person);
        order.setOrderId(1);
        order.setOrderProducts(list);
        order.setPaymentDone(true);
        String message = "Hello, Sergei Leonov!\n" +
                "Your order №1 has been successfully placed.\n" +
                "Your order:\n" +
                "name1 * 2 = 180 RUB\n" +
                "name2 * 1 = 200 RUB\n" +
                "Total: 380 RUB";

        when(orderRepository.findByOrderId(1)).thenReturn(Optional.of(order));
        when(personDetailsService.username(person)).thenReturn("Sergei Leonov");
        when(productService.getActualPrice(product1)).thenReturn(90);
        when(productService.getActualPrice(product2)).thenReturn(200);
        orderService.makePurchase(1);
        verify(emailService).sendEmail("email", "Order №1", message);
    }

    @Test
    public void testCheckCustomer() {
        Order order = new Order();
        Person person = new Person();
        person.setName("a");
        order.setPerson(person);
        when(personDetailsService.currentUser()).thenReturn(person);
        assertDoesNotThrow(() -> orderService.checkCustomer(order));
        when(personDetailsService.currentUser()).thenReturn(new Person());
        assertThrows(RuntimeException.class, () -> orderService.checkCustomer(order));
    }

    @Test
    public void testGetProduct() {
        Product product = new Product();
        product.setProductId(1);
        OrderProduct orderProduct = new OrderProduct(null, product, 1);
        assertEquals(product, orderService.getProduct(orderProduct));
    }

    @Test
    public void testGetOrderProductPrice() {
        Product product = new Product();
        product.setPrice(100);
        product.setDiscount(10);
        OrderProduct orderProduct = new OrderProduct(null, product, 2);
        when(productService.getActualPrice(product)).thenReturn(90);
        assertEquals(180, orderService.getOrderProductPrice(orderProduct));
    }

    @Test
    public void testBuildPurchaseEmail() {
        Person person = new Person();
        person.setName("Sergei");
        person.setSurname("Leonov");
        Product product1 = new Product("name1", "", 100, 10, 10);
        Product product2 = new Product("name2", "", 200, 10, 0);
        product1.setProductId(1);
        product2.setProductId(2);
        List<OrderProduct> list = new ArrayList<>();
        OrderProduct orderProduct1 = new OrderProduct(null, product1, 2);
        OrderProduct orderProduct2 = new OrderProduct(null, product2, 1);
        list.add(orderProduct1);
        list.add(orderProduct2);
        Order order = new Order(person);
        order.setOrderId(1);
        order.setOrderProducts(list);

        when(personDetailsService.username(person)).thenReturn("Sergei Leonov");
        when(productService.getActualPrice(product1)).thenReturn(90);
        when(productService.getActualPrice(product2)).thenReturn(200);
        String result = orderService.buildPurchaseEmail(order);
        String expected = "Hello, Sergei Leonov!\n" +
                "Your order №1 has been successfully placed.\n" +
                "Your order:\n" +
                "name1 * 2 = 180 RUB\n" +
                "name2 * 1 = 200 RUB\n" +
                "Total: 380 RUB";
        assertEquals(expected, result);
    }
}
