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

    const [localName, setLocalName] = React.useState(person.name);
    const [localSalary, setLocalSalary] = React.useState(person.salary * 100);
    const [localReserve, setLocalReserve] = React.useState(person.reservePercentage);

    const [displaySalary, setDisplaySalary] = React.useState(
        new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(person.salary)
    );

    React.useEffect(() => {
        setLocalName(person.name);
        setLocalSalary(person.salary * 100);
        setLocalReserve(person.reservePercentage);
        setDisplaySalary(
            new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(person.salary)
        );
    }, [person.name, person.salary, person.reservePercentage]);

    const handleSalaryChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const rawValue = e.target.value;

        if (rawValue === "") {
            setDisplaySalary("");
            setLocalSalary(0);
            return;
        }

        const digits = rawValue.replace(/\D/g, '');
        if (digits === "") {
            setDisplaySalary("");
            setLocalSalary(0);
            return;
        }

        const numericValue = Number(digits);
        setLocalSalary(numericValue);

        setDisplayAmount(
            new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(numericValue / 100)
        );
    };

    const handleSave = () => {
        const numericSalary = localSalary / 100;
        if (localName !== person.name || numericSalary !== person.salary || localReserve !== person.reservePercentage) {
            onUpdate(person.id, {
                name: localName,
                salary: numericSalary,
                reservePercentage: localReserve
            });
        }

        setDisplaySalary(
            new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(numericSalary)
        );
    };

    const setDisplayAmount = (value: string) => {
        setDisplaySalary(value);
    };

    return (
        <div className="group bg-white dark:bg-gray-800 border border-gray-100 dark:border-gray-700 rounded-2xl p-5 shadow-sm hover:shadow-md transition-all duration-300" data-testid={`person-card-list-${person.name}`}>
            <div className="mb-5">
                <label className="text-[10px] font-bold text-gray-400 dark:text-gray-500 uppercase tracking-widest block mb-1 ml-[1px]">Nome</label>
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
                            onClick={() => onRemove(person.id)}
                            className="flex-shrink-0 flex items-center justify-center font-bold text-sm bg-[#4f46e5] text-white hover:bg-[#4338ca] transition-all cursor-pointer rounded-xl h-11 px-3 opacity-100 md:opacity-0 md:group-hover:opacity-100"
                            title="Remover participante"
                            data-testid={`btn-remove-person-${person.name}`}
                        >
                            ✕
                        </button>
                    )}
                </div>
            </div>

            <div className="grid grid-cols-2 gap-6">
                <div className="space-y-1">
                    <label className="text-[10px] font-bold text-gray-400 dark:text-gray-500 uppercase tracking-widest block">Salário Mensal</label>
                    <div className="relative flex items-center">
                        <span className="text-gray-400 dark:text-gray-500 text-sm mr-2 font-medium">R$</span>
                        <input
                            type="text"
                            value={displaySalary}
                            onChange={handleSalaryChange}
                            onBlur={handleSave}
                            className="bg-transparent border-none px-1 w-full font-semibold text-gray-700 dark:text-gray-200 focus:ring-0 text-left"
                            placeholder="0,00"
                            data-testid={`input-card-person-salary-${person.name}`}
                        />
                    </div>
                </div>
                <div className="space-y-1">
                    <label className="text-[10px] font-bold text-gray-400 dark:text-gray-500 uppercase tracking-widest block">Reserva Desejada</label>
                    <div className="relative flex items-center">
                        <span className="text-gray-400 dark:text-gray-500 text-sm mr-2 font-medium">%</span>
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