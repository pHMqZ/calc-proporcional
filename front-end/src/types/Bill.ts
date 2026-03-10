export interface Bill {
    id: string;
    description: string;
    totalAmount: number;
}

export interface BillDistribution {
    billId: string;
    personId: string;
    amount: number;
    percentage: number;
}