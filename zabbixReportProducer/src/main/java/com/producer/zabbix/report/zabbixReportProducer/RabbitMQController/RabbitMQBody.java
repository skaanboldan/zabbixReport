package com.producer.zabbix.report.zabbixReportProducer.RabbitMQController;

import java.sql.Timestamp;


public class RabbitMQBody {
    String message;
    String mail;
    Timestamp date;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
