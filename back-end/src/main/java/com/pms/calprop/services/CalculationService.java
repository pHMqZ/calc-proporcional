package com.pms.calprop.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pms.calprop.dto.BillDistribution;
import com.pms.calprop.dto.CalculationSummaryResponse;
import com.pms.calprop.dto.PersonData;
import com.pms.calprop.dto.PersonResponse;
import com.pms.calprop.entities.Bill;
import com.pms.calprop.entities.Person;
import com.pms.calprop.mappers.PersonMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalculationService {

        private final PersonMapper personMapper;

        public BigDecimal calculateTotalSalary(List<Person> people) {

                return people.stream()
                                .map(Person::getSalary)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        public BigDecimal calculateTotalBills(List<Bill> bills) {
                return bills.stream()
                                .map(Bill::getTotalAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        public BigDecimal calculatePersonPercentage(Person person, BigDecimal totalSalary) {
                if (totalSalary.compareTo(BigDecimal.ZERO) == 0)
                        return BigDecimal.ZERO;

                return person.getSalary()
                                .multiply(new BigDecimal("100"))
                                .divide(totalSalary, 2, RoundingMode.HALF_UP);
        }

        public BigDecimal calculateReserveAmount(Person person) {
                BigDecimal percentegaFraction = BigDecimal.valueOf(person.getReservePercentage())
                                .divide(new BigDecimal("100"));

                return person.getSalary().multiply(percentegaFraction)
                                .setScale(2, RoundingMode.HALF_UP);
        }

        public List<BillDistribution> calculateBillsDistribution(List<Bill> bills, List<Person> people,
                        Map<Long, BigDecimal> percentages) {

                List<BillDistribution> billDistributions = new ArrayList<>();

                for (Bill bill : bills) {
                        BigDecimal sumOfAmountsCalculated = BigDecimal.ZERO;
                        for (int i = 0; i < people.size(); i++) {
                                Person person = people.get(i);
                                BigDecimal personPercentage = percentages.get(person.getId());

                                BigDecimal personPaysInBill;

                                if (i == people.size() - 1) {
                                        personPaysInBill = bill.getTotalAmount().subtract(sumOfAmountsCalculated);
                                } else {
                                        BigDecimal percentageFraction = personPercentage.divide(new BigDecimal("100"),
                                                        4,
                                                        RoundingMode.HALF_UP);
                                        personPaysInBill = bill.getTotalAmount().multiply(percentageFraction).setScale(
                                                        2,
                                                        RoundingMode.HALF_UP);

                                        sumOfAmountsCalculated = sumOfAmountsCalculated.add(personPaysInBill);
                                }

                                billDistributions.add(new BillDistribution(bill.getId(), bill.getDescription(),
                                                person.getId(),
                                                personPaysInBill, personPercentage));

                        }

                }

                return billDistributions;
        }

        public BigDecimal calculatePersonBillsTotal(Person person, List<BillDistribution> billsDistribution) {

                return billsDistribution.stream()
                                .filter(distribution -> distribution.personId().equals(person.getId()))
                                .map(BillDistribution::amount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        public PersonData calculatePersonData(Person person, BigDecimal percentage,
                        List<BillDistribution> billDistributions) {

                PersonResponse response = personMapper.toResponse(person);
                BigDecimal reserveAmount = calculateReserveAmount(person);
                BigDecimal billsTotal = calculatePersonBillsTotal(person, billDistributions);
                BigDecimal totalToPay = billsTotal.add(reserveAmount);
                BigDecimal remainingSalary = person.getSalary().subtract(totalToPay);

                List<BillDistribution> personBillDistributions = billDistributions.stream()
                                .filter(distribution -> distribution.personId().equals(person.getId()))
                                .toList();

                return new PersonData(response, percentage, reserveAmount, billsTotal, totalToPay, remainingSalary,
                                personBillDistributions);
        }

        public List<PersonData> calculateAllPeopleData(List<Person> people, Map<Long, BigDecimal> percentages,
                        List<BillDistribution> billDistributions) {
                return people.stream()
                                .map(person -> calculatePersonData(person, percentages.get(person.getId()),
                                                billDistributions))
                                .toList();
        }

        public CalculationSummaryResponse calculateSummaryData(List<Bill> bills, List<Person> people) {

                BigDecimal totalSalary = calculateTotalSalary(people);
                BigDecimal totalBills = calculateTotalBills(bills);

                Map<Long, BigDecimal> percentages = people.stream()
                                .collect(Collectors.toMap(Person::getId,
                                                p -> calculatePersonPercentage(p, totalSalary)));

                var distributions = calculateBillsDistribution(bills, people, percentages);

                var peopleData = people.stream()
                                .map(p -> calculatePersonData(p, percentages.get(p.getId()), distributions))
                                .toList();

                BigDecimal totalReserve = peopleData.stream()
                                .map(PersonData::reserveAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalWithReserve = totalBills.add(totalReserve);
                BigDecimal totalRemainder = totalSalary.subtract(totalWithReserve);

                return new CalculationSummaryResponse(
                                totalSalary,
                                totalBills,
                                totalWithReserve,
                                totalRemainder,
                                peopleData,
                                distributions);
        }

}
