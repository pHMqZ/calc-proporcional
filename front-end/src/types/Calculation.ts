import type { BillDistribution } from "./Bill";
import type { PersonCalculation } from "./Person";

export interface CalculationSummary {
    totalSalary: number;
    totalBills: number;
    totalWithReserve: number;
    totalRemainder: number;
    peopleData: PersonCalculation[];
    billDistribution: BillDistribution[];
}