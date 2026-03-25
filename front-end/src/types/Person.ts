export interface Person {
    id: string;
    name: string;
    salary: number;
    reservePercentage: number;
}

export interface PersonCalculation extends Person {
    percentage: number;
    reserveAmount: number;
    billsTotal: number;
    totalWithReserve: number;
    remaining: number;
}