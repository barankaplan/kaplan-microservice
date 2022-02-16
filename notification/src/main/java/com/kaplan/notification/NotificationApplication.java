package com.kaplan.notification;

import com.kaplan.amqp.RabbitMQMessageProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(scanBasePackages = {"com.kaplan.notification",
        "com.kaplan.amqp"
})
@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
})
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(RabbitMQMessageProducer rabbitMQMessageProducer,NotificationConfiguration notificationConfiguration){
//        return args -> {
//            rabbitMQMessageProducer.publish(
//                    "foo", notificationConfiguration.getInternalExchange(),
//                    notificationConfiguration.getInternalNotificationRoutingKey());
        return args -> {
            rabbitMQMessageProducer.publish(
                    new Person("Baran",32), notificationConfiguration.getInternalExchange(),
                    notificationConfiguration.getInternalNotificationRoutingKey());
        };
    }

    record Person(String name, int age){}
}