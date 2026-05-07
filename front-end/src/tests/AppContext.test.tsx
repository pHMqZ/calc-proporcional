import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { AppProvider, useAppContext } from '../context/AppContext';
import * as api from '../services/api';
import React from 'react';


vi.mock('../services/api', () => ({
    api: {
        getSummary: vi.fn(),
        getPeople: vi.fn(),
        getBills: vi.fn(),
        createPerson: vi.fn(),
        createBill: vi.fn(),
        updatePerson: vi.fn(),
        updateBill: vi.fn(),
        deletePerson: vi.fn(),
        deleteBill: vi.fn(),
    }
}));

const TestComponent = () => {
    const { summary, isLoading } = useAppContext();
    if (isLoading) return <div>Carregando...</div>;
    return <div>Total Bills: {summary?.totalBills}</div>;
};

describe('AppContext Integration', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('Should load the summary data when the Provider starts', async () => {

        (api.api.getSummary as any).mockResolvedValue({
            totalBills: 1250.50,
            totalSalary: 5000,
            totalWithReserve: 4500,
            peopleData: []
        });
        (api.api.getPeople as any).mockResolvedValue([]);
        (api.api.getBills as any).mockResolvedValue([]);

        render(
            <AppProvider>
                <TestComponent />
            </AppProvider>
        );

        await waitFor(() => {
            expect(screen.getByText(/Total Bills: 1250.5/)).toBeInTheDocument();
        }, { timeout: 2000 });

        expect(api.api.getSummary).toHaveBeenCalledTimes(1);
    });
});
