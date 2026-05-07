import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { BillRow } from '../components/bills/billsRow';

describe('Validate BillRow component', () => {
    const mockBill = { id: 10, description: 'Energia', totalAmount: 200 };
    const mockPeopleData = [
        {
            person: { id: 1, name: 'Alceu' },
            percentage: 50,
            billDistributions: [{ billId: 10, amount: 100 }]
        }
    ];
    const mockOnUpdate = vi.fn();
    const mockOnRemove = vi.fn();

    it('Should render bill data correctly', () => {
        render(
            <table>
                <tbody>
                    <BillRow
                        bill={mockBill}
                        peopleData={mockPeopleData}
                        onUpdate={mockOnUpdate}
                        onRemove={mockOnRemove}
                    />
                </tbody>
            </table>
        );

        expect(screen.getByDisplayValue('Energia')).toBeInTheDocument();
        expect(screen.getByDisplayValue('200,00')).toBeInTheDocument();
    });

    it('Should display the shared value correctly for the participant', () => {
        render(
            <table>
                <tbody>
                    <BillRow
                        bill={mockBill}
                        peopleData={mockPeopleData}
                        onUpdate={mockOnUpdate}
                        onRemove={mockOnRemove}
                    />
                </tbody>
            </table>
        );

        expect(screen.getByText(/100,00/)).toBeInTheDocument();
    });
});
