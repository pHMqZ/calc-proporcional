import type { BillDistribution } from "./Bill";
import type { PersonData } from "./Person";

export interface CalculationSummary {
    totalSalary: number;
    totalBills: number;
    totalWithReserve: number;
    totalRemainder: number;
    peopleData: PersonData[];
    billsDistribution: BillDistribution[];
}