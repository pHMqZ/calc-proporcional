import type { Bill, BillDistribution } from "../types/Bill";
import type { Person, PersonCalculation } from "../types/Person";

export class CalculationService {
    static calculateTotalSalary(people: Person[]): number {
        return people.reduce((sum, person) => sum + person.salary, 0);
    }

    static calculatePersonPercentage(person: Person, totalSalary: number): number {
        return totalSalary > 0 ? person.salary / totalSalary : 0;
    }

    static calculateReserveAmount(person: Person): number {
        return person.salary * person.reservePercentage;
    }

    static calculateBillDistribution(
        bills: Bill[],
        people: Person[],
        totalSalary: number
    ): BillDistribution[] {
        const distributions: BillDistribution[] = [];

        bills.forEach((bill => {
            people.forEach(person => {
                const percentage = this.calculatePersonPercentage(person, totalSalary);
                const amount = bill.totalAmount * percentage;

                distributions.push({
                    billId: bill.id,
                    personId: person.id,
                    amount,
                    percentage
                });
            });
        }));

        return distributions;
    }

    static calculatePersonBillsTotal(
        personId: string,
        distributions: BillDistribution[]
    ): number {
        return distributions
            .filter(d => d.personId === personId)
            .reduce((sum, d) => sum + d.amount, 0);
    }
    
    static calculateTotalBills(bills: Bill[]): number {
        return bills.reduce((sum, bill) => sum + bill.totalAmount, 0);
    }

    static calculatePersonData(
        person: Person,
        totalSalary: number,
        distribuitions: BillDistribution[]
    ): PersonCalculation {
        const percentage = this.calculatePersonPercentage(person, totalSalary);
        const reserveAmount = this.calculateReserveAmount(person);
        const billsTotal = this.calculatePersonBillsTotal(person.id, distribuitions);
        const totalWithReserve = billsTotal + reserveAmount;
        const remaining = person.salary - totalWithReserve;

        return {
            ...person,
            percentage,
            reserveAmount,
            billsTotal,
            totalWithReserve,
            remaining
        };
    }

    static calculateAllPeopleData(
        people: Person[],
        bills: Bill[]
    ): PersonCalculation[] {
        const totalSalary = this.calculateTotalSalary(people);
        const distributions = this.calculateBillDistribution(bills, people, totalSalary);
        
        return people.map(person => this.calculatePersonData(person, totalSalary, distributions));
    }
    
}