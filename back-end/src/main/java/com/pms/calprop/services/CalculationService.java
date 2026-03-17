package com.pms.calprop.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pms.calprop.entities.Person;

@Service
public class CalculationService {

    public BigDecimal calculateTotalSalary(List<Person> people) {

        BigDecimal totalSalary = BigDecimal.ZERO;

        for (Person person : people) {
            totalSalary = totalSalary.add(person.getSalary());
        }

        return totalSalary;

    }

    public BigDecimal calculatePersonPercentage(Person person, BigDecimal totalSalary) {
        BigDecimal personPercentage = BigDecimal.ZERO;

        if (totalSalary.compareTo(BigDecimal.ZERO) == 0) {
            return personPercentage;
        } else {
            personPercentage = (person.getSalary().multiply(new BigDecimal("100")).divide(totalSalary, 2,
                    RoundingMode.HALF_UP));

            return personPercentage;
        }
    }

}
