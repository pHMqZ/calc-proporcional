package com.pms.calprop.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name="persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private  Number salary;

    private Number reservePercentage;

    public  Person() {

    }

    public Person(Long id, String name, Number salary, Number reservePercentage) {
        this.id = id;
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

    public Number getSalary() {
        return salary;
    }

    public void setSalary(Number salary) {
        this.salary = salary;
    }

    public Number getReservePercentage() {
        return reservePercentage;
    }

    public void setReservePercentage(Number reservePercentage) {
        this.reservePercentage = reservePercentage;
    }
}
