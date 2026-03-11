package com.pms.calprop.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name="bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Number totalAmount;

    public Bill() {

    }

    public Bill(Long id, String description, Number totalAmount) {
        this.id = id;
        this.description = description;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Number getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Number totalAmount) {
        this.totalAmount = totalAmount;
    }
}
