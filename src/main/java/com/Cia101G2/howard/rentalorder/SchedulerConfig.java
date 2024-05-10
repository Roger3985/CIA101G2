package com.Cia101G2.howard.rentalorder;

import com.Cia101G2.Entity.RentalOrder;
import com.Cia101G2.howard.rentalorder.dao.RentalOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    RentalOrderRepository orderRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void performTaskWithSet1() {

        List<RentalOrder> orders = orderRepository.findAll();

    }

}
