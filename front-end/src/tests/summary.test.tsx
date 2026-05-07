import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import { useAppContext } from '../context/AppContext';
import { Summary } from '../components/summary';

vi.mock('../context/AppContext', () => ({
    useAppContext: vi.fn()
}));

describe('Validate Summary Component', () => {
    it('Should display balance in red when value is negative', () => {
        (useAppContext as any).mockReturnValue({
            summary: {
                totalBills: 1000,
                totalSalary: 2000,
                totalWithReserve: 1800,
                peopleData: [
                    {
                        person: { id: 1, name: 'Alceu' },
                        percentage: 50,
                        reserveAmount: 200,
                        remainingSalary: -100.50,
                        totalToPay: 700.50,
                        billDistributions: []
                    }
                ]
            },
            isLoading: false
        });

        render(<Summary />);

        const balanceValue = screen.getByText(/-R\$.*100,50/);

        expect(balanceValue).toBeInTheDocument();

        expect(balanceValue).toHaveClass('text-red-700');
    });
});