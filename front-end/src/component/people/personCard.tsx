import type React from "react";
import { type Person } from '../../types/Person';


interface PersonCardProps {
    person: Person;
    onUpdate: (id: string, updates: Partial<Person>) => void;
    onRemove: (id: string) => void;
    showRemove?: boolean;
}

export const PersonCard: React.FC<PersonCardProps> = ({
    person,
    onUpdate,
    onRemove,
    showRemove = true
}) => {
    return (
        <div className="person-card bg-[#f9fafb] border border-[#e5e7eb] rounded-xl p-4">
            <div className="flex justify-between items-start mb-3">
                <input
                    type="text"
                    value={person.name}
                    onChange={(e) => onUpdate(person.id, { name: e.target.value })}
                    className="text-lg font-semibold bg-transparent border-none outline-none p-0 flex-1"
                    placeholder="Nome"
                />
                {showRemove && (
                    <button
                        className="secondary text-xl leading-none px-2 py-1 ml-2"
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
                        step="0.01"
                        value={person.salary || ''}
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
                        value={person.reservePercentage || ''}
                        onChange={(e) =>
                            onUpdate(person.id, { reservePercentage: Number(e.target.value) })}
                        placeholder="0"
                    />
                </div>
            </div>
        </div>
    );
}
