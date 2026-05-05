import type { BillDistribution } from "./Bill";

export interface Person {
    id: number;
    name: string;
    salary: number;
    reservePercentage: number;
}

export interface PersonCalculation {
    person: Person;
    percentage: number;
    reserveAmount: number;
    billsTotal: number;
    totalToPay: number;
    remainingSalary: number;
    billsDistribuitions: BillDistribution[];
}