export interface Bill {
    id: string;
    description: string;
    totalAmount: number;
}

export interface BillDistribution {
    billId: string;
    billDescription: string;
    personId: string;
    amount: number;
    percentage: number;
}