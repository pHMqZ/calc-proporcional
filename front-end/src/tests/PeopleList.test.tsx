import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import { useAppContext } from '../context/AppContext';
import { PeopleList } from '../components/people/peopleList';

vi.mock('../context/AppContext', () => ({
    useAppContext: vi.fn()
}))

describe('Validate PeopleList Component', () => {

    it('Should display empty state message when there are no people', () => {
        (useAppContext as any).mockReturnValue({
            people: [],
            summary: {
                totalSalary: 0,
                totalBills: 0,
                totalWithReserve: 0,
                peopleData: []
            },
            isLoading: false,
            addPerson: vi.fn(),
            updatePerson: vi.fn(),
            deletePerson: vi.fn()
        });

        render(<PeopleList />)

        const emptyParticipantsMessage = screen.getByText(/Nenhum participante cadastrado/i);
        expect(emptyParticipantsMessage).toBeInTheDocument();

        const emptyCalculationMessage = screen.getByText(/Cadastre participantes para visualizar o cálculo/i);
        expect(emptyCalculationMessage).toBeInTheDocument();

        const distributionTitle = screen.queryByTestId('distribution-bills-title');
        const reserveTitle = screen.queryByText('Valor Reservado');

        expect(distributionTitle).toBeNull();
        expect(reserveTitle).toBeNull();
    })

    it('Should display participants when there are participants', () => {
        const mockPeople = [
            { id: 1, name: "Alceu", salary: 1000, participation: 50 },
            { id: 2, name: "Toalha", salary: 2000, participation: 50 }
        ];

        (useAppContext as any).mockReturnValue({
            people: mockPeople,
            summary: {
                totalSalary: 3000,
                totalBills: 0,
                totalWithReserve: 0,
                peopleData: [
                    {
                        person: {
                            id: 1,
                            name: "Alceu",
                            salary: 1000,
                            participation: 50
                        }, billsTotal: 0, percentage: 50
                    },
                    {
                        person: {
                            id: 2,
                            name: "Toalha",
                            salary: 2000,
                            participation: 50
                        }, billsTotal: 0, percentage: 50
                    }
                ]
            },
            isLoading: false,
            addPerson: vi.fn(),
            updatePerson: vi.fn(),
            deletePerson: vi.fn()
        });

        render(<PeopleList />)

        expect(screen.getByTestId("input-card-list-person-name-Alceu")).toBeInTheDocument();
        expect(screen.getByTestId("input-card-list-person-name-Toalha")).toBeInTheDocument();
    })
})