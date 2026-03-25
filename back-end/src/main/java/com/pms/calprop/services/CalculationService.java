package com.pms.calprop.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pms.calprop.dto.BillDistribution;
import com.pms.calprop.dto.PersonData;
import com.pms.calprop.entities.Bill;
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

    public BigDecimal calculateReserveAmount(Person person) {

        BigDecimal percentegaFraction = BigDecimal.valueOf(person.getReservePercentage())
                .divide(new BigDecimal("100"));

        return person.getSalary().multiply(percentegaFraction)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateTotalBills(List<Bill> bills) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Bill bill : bills) {
            totalAmount = totalAmount.add(bill.getTotalAmount());
        }

        return totalAmount;
    }

    public List<BillDistribution> calculateBillsDistribuition(List<Bill> bills, List<Person> people) {
        List<BillDistribution> billDistributions = new ArrayList<>();
        BigDecimal totalSalary = calculateTotalSalary(people);

        for (Bill bill : bills) {
            BigDecimal sumOfAmountsCalculated = BigDecimal.ZERO;
            for (int i = 0; i < people.size(); i++) {
                Person person = people.get(i);
                BigDecimal personPercentage = calculatePersonPercentage(person, totalSalary);

                BigDecimal personPaysInBill;

                if (i == people.size() - 1) {
                    personPaysInBill = bill.getTotalAmount().subtract(sumOfAmountsCalculated);
                } else {
                    BigDecimal percentageFraction = personPercentage.divide(new BigDecimal("100"), 4,
                            RoundingMode.HALF_UP);
                    personPaysInBill = bill.getTotalAmount().multiply(percentageFraction).setScale(2,
                            RoundingMode.HALF_UP);

                    sumOfAmountsCalculated = sumOfAmountsCalculated.add(personPaysInBill);
                }

                billDistributions
                        .add(new BillDistribution(bill.getId(), person.getId(), personPaysInBill, personPercentage));

            }

        }

        return billDistributions;
    }

    public BigDecimal calculatePersonBillsTotal(Person person, List<BillDistribution> billsDistribuition) {

        return billsDistribuition.stream()
                .filter(distribuition -> distribuition.personId().equals(person.getId()))
                .map(BillDistribution::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public PersonData calculatePersonData(Person person, BigDecimal totalSalary,
            List<BillDistribution> billDistributions) {

        BigDecimal personPercentage = calculatePersonPercentage(person, totalSalary);
        BigDecimal reserveAmount = calculateReserveAmount(person);
        BigDecimal billsTotal = calculatePersonBillsTotal(person, billDistributions);
        BigDecimal totalToPay = billsTotal.add(reserveAmount);
        BigDecimal remainingSalary = person.getSalary().subtract(totalToPay);

        List<BillDistribution> personBillDistributions = billDistributions.stream()
                .filter(distribuition -> distribuition.personId().equals(person.getId()))
                .toList();

        return new PersonData(person, personPercentage, reserveAmount, billsTotal, totalToPay, remainingSalary,
                personBillDistributions);
    }

    public List<PersonData> calculateAllPeopleData(List<Person> people, BigDecimal totalSalary,
            List<BillDistribution> billDistributions) {
        List<PersonData> peopleData = new ArrayList<>();

        for (Person person : people) {
            peopleData.add(calculatePersonData(person, totalSalary, billDistributions));
        }

        return peopleData;
    }

}
