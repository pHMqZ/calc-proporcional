package com.pms.calprop.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "people")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty(message = "Fill in the person's name")
    private String name;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true, message = "The salary must be 0 or greater")
    @Column(precision = 38, scale = 2)
    private BigDecimal salary;

    @Min(0)
    @Max(100)
    @NotNull
    private Double reservePercentage;

    @Column(name = "client_id", nullable = false, updatable = false)
    private String clientId;

    public Person(String name, BigDecimal salary, Double reservePercentage, String clientId) {
        this.name = name;
        this.salary = salary;
        this.reservePercentage = reservePercentage;
        this.clientId = clientId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Double getReservePercentage() {
        return reservePercentage;
    }

    public void setReservePercentage(Double reservePercentage) {
        this.reservePercentage = reservePercentage;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
