/* eslint-disable @typescript-eslint/no-explicit-any */
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

    it('Should disable Generate Image button when there is no data', () => {
        (useAppContext as any).mockReturnValue({
            summary: {
                totalBills: 0,
                totalSalary: 0,
                totalWithReserve: 0,
                peopleData: []
            },
            isLoading: false
        });

        render(<Summary />);

        const generateImageButton = screen.getByTestId('generate-image-button');

        expect(generateImageButton).toBeInTheDocument();
        expect(generateImageButton).toBeDisabled();
    });

    it('Should enable Generate Image button when there is data', () => {
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

        const generateImageButton = screen.getByTestId('generate-image-button');

        expect(generateImageButton).toBeInTheDocument();
        expect(generateImageButton).toBeEnabled();
    });

    it('Should display empty state messages when there are no participants', () => {
        (useAppContext as any).mockReturnValue({
            summary: {
                totalBills: 0,
                totalSalary: 0,
                totalWithReserve: 0,
                peopleData: []
            },
            isLoading: false
        });

        render(<Summary />);

        const payStateMessage = screen.getByText(/Adicione participantes para visualizar o rateio\./i);
        const remainingStateMessage = screen.getByText(/Adicione participantes para visualizar os saldos\./i)

        expect(payStateMessage).toBeInTheDocument();
        expect(remainingStateMessage).toBeInTheDocument();

        const personSection = screen.queryByTestId('summary-person-section');
        const remainingSection = screen.queryByTestId('summary-remaining-section');

        expect(personSection).toBeNull();
        expect(remainingSection).toBeNull();
    });

    it('Should display person amount and remaining salary correctly', () => {
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

        const personAmount = screen.getByTestId('summary-person-amount');
        const remainingAmount = screen.getByTestId('summary-remaining-amount');

        expect(personAmount).toHaveTextContent('R$ 700,50');
        expect(remainingAmount).toHaveTextContent('-R$ 100,50');
    });

});