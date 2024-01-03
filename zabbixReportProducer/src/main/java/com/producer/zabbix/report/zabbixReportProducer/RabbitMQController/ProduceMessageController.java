package com.producer.zabbix.report.zabbixReportProducer.RabbitMQController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.producer.zabbix.report.zabbixReportProducer.RabbitMQController.ProduceMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Timestamp;

@RestController
public class ProduceMessageController {

    @Autowired
    ProduceMessageService produceMessageService;

    @PostMapping("/produce")
    public String produceMessage(@RequestParam String mail,@RequestParam String message) throws JsonProcessingException {
        RabbitMQBody rabbitMQBody=new RabbitMQBody();
        rabbitMQBody.setMessage(message);
        rabbitMQBody.setMail(mail);
        rabbitMQBody.setDate( new Timestamp(System.currentTimeMillis()));
        ObjectMapper objectMapper=new ObjectMapper();

        return produceMessageService.produceMessage(objectMapper.writeValueAsString(rabbitMQBody));
    }
}