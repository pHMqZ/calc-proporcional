import type React from "react";
import { type Person } from '../../types/Person';


interface PersonCardProps {
    person: Person;
    onUpdate: (id: number, updates: Partial<Person>) => Promise<void>;
    onRemove: (id: number) => Promise<void>;
    showRemove?: boolean;
}

export const PersonCard: React.FC<PersonCardProps> = ({
    person,
    onUpdate,
    onRemove,
    showRemove = true
}) => {

    // Função para formatar o valor numérico para string com máscara BRL
    const formatBRL = (value: number) => {
        return new Intl.NumberFormat('pt-BR', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }).format(value);
    };

    // Função para transformar a string com máscara de volta em número
    const handleSalaryChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value.replace(/\D/g, '');
        const numericValue = Number(value) / 100;
        onUpdate(person.id, { salary: numericValue });
    };

    return (
        <div className="group bg-white dark:bg-gray-800 border border-gray-100 dark:border-gray-700 rounded-2xl p-5 shadow-sm hover:shadow-md transition-all duration-300">
            <div className="mb-5">
                <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest block mb-1 ml-[1px]">Nome</label>
                <div className="flex justify-between items-center gap-6">
                    <div className="flex-1">
                        <input
                            type="text"
                            value={person.name}
                            onChange={(e) => onUpdate(person.id, { name: e.target.value })}
                            className="text-xl font-bold bg-transparent border-none outline-none p-0 w-full text-gray-800 dark:text-white focus:ring-0"
                            placeholder="Nome"
                        />
                    </div>
                    {showRemove && (
                        <button
                            className="text-gray-300 hover:text-red-700 hover:bg-red-50 dark:hover:bg-red-900/20 transition-all p-2 rounded-lg opacity-0 group-hover:opacity-100"
                            onClick={() => onRemove(person.id)}
                            title="Remover participante"
                        >
                            <span className="text-lg">✕</span>
                        </button>
                    )}
                </div>
            </div>

            <div className="grid grid-cols-2 gap-6">
                <div className="space-y-1">
                    <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest block">Salário Mensal</label>
                    <div className="relative flex items-center">
                        <span className="text-gray-400 text-sm mr-2 font-medium">R$</span>
                        <input
                            type="text"
                            value={formatBRL(person.salary)}
                            onChange={handleSalaryChange}
                            className="bg-transparent border-none p-0 w-full font-semibold text-gray-700 dark:text-gray-200 focus:ring-0 text-left"
                            placeholder="0,00"
                        />
                    </div>
                </div>
                <div className="space-y-1">
                    <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest block">Reserva Desejada</label>
                    <div className="relative flex items-center">
                        <span className="text-gray-400 text-sm mr-2 font-medium">%</span>
                        <input
                            type="number"
                            min="0"
                            max="100"
                            value={person.reservePercentage || ''}
                            onChange={(e) => onUpdate(person.id, { reservePercentage: Number(e.target.value) })}
                            className="bg-transparent border-none p-0 w-full font-semibold text-gray-700 dark:text-gray-200 focus:ring-0 text-left"
                            placeholder="0"
                        />
                    </div>
                </div>
            </div>
        </div>
    );
}
