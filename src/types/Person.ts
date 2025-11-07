export interface Person {
    id: number;
    name: string;
    salary: number;
    reservePercentage: number;
}

export interface PersonCalculations extends Person {
    percentage: number;
    reserveAmount: number;
    billsTotal: number;
    totalWithReserve: number;
    remaining: number;
}