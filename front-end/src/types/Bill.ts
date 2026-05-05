export interface Bill {
    id: number;
    description: string;
    totalAmount: number;
}

export interface BillDistribution {
    billId: number;
    billDescription: string;
    personId: number;
    amount: number;
    percentage: number;
}