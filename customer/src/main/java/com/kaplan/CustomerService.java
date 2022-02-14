package com.kaplan;


import com.kaplan.amqp.RabbitMQMessageProducer;
import com.kaplan.clients.notification.NotificationClient;
import com.kaplan.clients.notification.NotificationRequest;
import com.kaplan.clients.fraud.FraudCheckResponse;
import com.kaplan.clients.fraud.FraudClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final RestTemplate restTemplate;
    private final FraudClient fraudClient;

    private final NotificationClient notificationClient;

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse =
                fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }


        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to my microservice",
                        customer.getFirstName())
        );
        rabbitMQMessageProducer.publish(notificationRequest,"internal.exchange",
                "internal.notification.routing-key");

//        notificationClient.sendNotification(
//                notificationRequest
//        );
    }
}
