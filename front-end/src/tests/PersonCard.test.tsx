import { describe, expect, it, vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import { PersonCard } from "../components/people/personCard";

describe('Validate person card component', () => {
    const person = { id: 1, name: "Alceu", salary: 3000, reservePercentage: 10 };
    const mockOnUpdate = vi.fn();
    const mockOnRemove = vi.fn();

    it('Should render person data correctly', () => {
        render(<PersonCard person={person} onUpdate={mockOnUpdate} 
            onRemove={mockOnRemove} />)

        expect(screen.getByDisplayValue('Alceu')).toBeInTheDocument();
        expect(screen.getByDisplayValue('3.000,00')).toBeInTheDocument();
        expect(screen.getByDisplayValue('10')).toBeInTheDocument();
    });

    it('Should call onUpdate with correctly parsed salary (Masking logic)', () => {
        render(<PersonCard person={person} onUpdate={mockOnUpdate}
            onRemove={mockOnRemove} />)

        const salaryInput = screen.getByDisplayValue('3.000,00');

        // Se o usuário digita "3000", a máscara remove não-dígitos e divide por 100
        fireEvent.change(salaryInput, { target: { value: '3000' } });
        
        // Esperamos que o backend receba 30.00
        expect(mockOnUpdate).toHaveBeenCalledWith(1, expect.objectContaining({ salary: 30 }));
    });

    it('Should show remove button only when showRemove is true', () => {
        const { rerender } = render(<PersonCard person={person} onUpdate={mockOnUpdate}
            onRemove={mockOnRemove} showRemove={false} />)

        expect(screen.queryByTitle('Remover participante')).not.toBeInTheDocument();

        rerender(<PersonCard person={person} onUpdate={mockOnUpdate}
            onRemove={mockOnRemove} showRemove={true} />);

        expect(screen.getByTitle('Remover participante')).toBeInTheDocument();
    });
});