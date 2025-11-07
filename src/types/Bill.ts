export interface Bill {
    id: string;
    description: string;
    totalAmount: number;
}

export interface BillDistribution {
    billId: string;
    personId: number;
    amount: number;
    percentage: number;
}