import React from "react";
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

    // 1. Estados Locais
    const [localName, setLocalName] = React.useState(person.name);
    const [localSalary, setLocalSalary] = React.useState(person.salary * 100);
    const [localReserve, setLocalReserve] = React.useState(person.reservePercentage);
    // 2. Sincronizar se os dados mudarem externamente
    React.useEffect(() => {
        setLocalName(person.name);
        setLocalSalary(person.salary * 100);
        setLocalReserve(person.reservePercentage);
    }, [person.name, person.salary, person.reservePercentage]);
    // 3. Função de Salvamento Única
    const handleSave = () => {
        const numericSalary = localSalary / 100;
        if (localName !== person.name || numericSalary !== person.salary || localReserve !== person.reservePercentage) {
            onUpdate(person.id, {
                name: localName,
                salary: numericSalary,
                reservePercentage: localReserve
            });
        }
    };

    return (
        <div className="group bg-white dark:bg-gray-800 border border-gray-100 dark:border-gray-700 rounded-2xl p-5 shadow-sm hover:shadow-md transition-all duration-300" data-testid={`person-card-list-${person.name}`}>
            <div className="mb-5">
                <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest block mb-1 ml-[1px]">Nome</label>
                <div className="flex justify-between items-center gap-6">
                    <div className="flex-1">
                        <input
                            type="text"
                            value={localName}
                            onChange={(e) => setLocalName(e.target.value)}
                            onBlur={handleSave}
                            className="text-xl font-bold bg-transparent border-none outline-none px-2 py-1 w-full text-gray-800 dark:text-white focus:ring-0"
                            placeholder="Nome"
                            data-testid={`input-card-list-person-name-${person.name}`}
                        />
                    </div>
                    {showRemove && (
                        <button
                            className="text-gray-300 hover:text-red-700 hover:bg-red-50 dark:hover:bg-red-900/20 transition-all p-2 rounded-lg opacity-0 group-hover:opacity-100"
                            onClick={() => onRemove(person.id)}
                            title="Remover participante"
                            data-testid={`btn-remove-person-${person.name}`}
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
                            value={new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(localSalary / 100)}
                            onChange={(e) => setLocalSalary(Number(e.target.value.replace(/\D/g, '')))}
                            onBlur={handleSave}
                            className="bg-transparent border-none px-1 w-full font-semibold text-gray-700 dark:text-gray-200 focus:ring-0 text-left"
                            placeholder="0,00"
                            data-testid={`input-card-person-salary-${person.name}`}
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
                            value={localReserve || ''}
                            onChange={(e) => setLocalReserve(Number(e.target.value))}
                            onBlur={handleSave}
                            className="bg-transparent border-none px-1 w-full font-semibold text-gray-700 dark:text-gray-200 focus:ring-0 text-left"
                            placeholder="0"
                            data-testid={`input-card-person-reserve-${person.name}`}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
}
