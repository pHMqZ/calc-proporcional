import type React from "react";
import { type Person } from '../../types/Person';


interface PersonCardProps {
    person: Person;
    onUpdate: (id: string, updates: Partial<Person>) => void;
    onRemove: (id: string) => void;
    showRemove?: boolean;
}

export const PersonCard:  React.FC<PersonCardProps> = ({
    person,
    onUpdate,
    onRemove,
    showRemove = true
    }) => {
        const BRL = Intl.NumberFormat("pt-BR", {
            style: "currency",
            currency: "BRL",
        });

        return (
            <div className="person-card">
                <div className="flex justify-between items-center mb-3">
                    <input
                        type="text"
                        value={person.name}
                        onChange={(e) => onUpdate(person.id, { name: e.target.value })}
                        className="text-lg font-semibold bg-transparent border-none outline-none"
                        placeholder="Nome" 
                    />
                    { showRemove && (
                        <button
                            className="secondary text-xl leading-none px-2 py-1"
                            onClick={() => onRemove(person.id)}
                            title="Remover pessoa"
                        >
                            X
                        </button> 
                    )}
                </div>
                <div className="space-y-3">
                    <div>
                        <label className="text-xs text-[#6b7280] block mb-1">Salário</label>
                        <input 
                            type="number"
                            value={person.salary}
                            onChange={(e) => onUpdate(person.id, { salary: Number(e.target.value) })}
                            placeholder="0.00"
                            />
                    </div>
                    <div>
                        <label className="text-xs text-[#6b7280] block mb-1">Reserva (%)</label>
                        <input 
                            type="number"
                            min="0"
                            max="100"
                            value={person.reservePercentage}
                            onChange={(e) => 
                                onUpdate(person.id, { reservePercentage: Number(e.target.value) })}
                            placeholder="0"
                        />
                    </div>
                </div>
            </div>
        );
    }
