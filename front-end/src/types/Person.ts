import type { BillDistribution } from "./Bill";

export interface Person {
    id: string;
    name: string;
    salary: number;
    reservePercentage: number;
}

export interface PersonData {
    person: Person;
    percentage: number;
    reserveAmount: number;
    billsTotal: number;
    totalToPay: number;
    remainingSalary: number;
    billDistributions: BillDistribution[];
}