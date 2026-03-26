package com.pms.calprop.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "people")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Fill in the person's name")
    private String name;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false, message = "The salary must be greater than zero")
    private BigDecimal salary;

    @Min(0)
    @Max(100)
    @NotNull
    private Double reservePercentage;

    public Person(String name, BigDecimal salary, Double reservePercentage) {
        this.name = name;
        this.salary = salary;
        this.reservePercentage = reservePercentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
