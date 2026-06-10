import React, { useState } from "react";
import { useAppContext } from "../../context/AppContext";
import { PersonCard } from "./personCard";

export const PeopleList: React.FC = () => {
    const { people, addPerson, updatePerson, deletePerson, summary, isLoading } = useAppContext();
    const [newPersonName, setNewPersonName] = useState("");
    const [newPersonSalary, setNewPersonSalary] = useState<number>(0);
    const [newPersonReserve, setNewPersonReserve] = useState<number>(0);

    const [displaySalary, setDisplaySalary] = useState("0,00");
    const [displayReserve, setDisplayReserve] = useState("0,00");
    const [showAddForm, setShowAddForm] = useState(false);

    const BRL = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL",
    });

    const PC = new Intl.NumberFormat("pt-BR", {
        minimumFractionDigits: 1,
        maximumFractionDigits: 1,
    });

    const handleSalaryChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const rawValue = e.target.value;

        if (rawValue === "") {
            setDisplaySalary("");
            setNewPersonSalary(0);
            return;
        }

        const digits = rawValue.replace(/\D/g, '');
        if (digits === "") {
            setDisplaySalary("");
            setNewPersonSalary(0);
            return;
        }

        const numericValue = Number(digits);
        setNewPersonSalary(numericValue / 100);
        setDisplaySalary(
            new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(numericValue / 100)
        );
    };

    const handleReserveChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const rawValue = e.target.value;

        if (rawValue === "") {
            setDisplayReserve("");
            setNewPersonReserve(0);
            return;
        }

        const digits = rawValue.replace(/\D/g, '');
        if (digits === "") {
            setDisplayReserve("");
            setNewPersonReserve(0);
            return;
        }

        const numericValue = Number(digits);
        const percentageValue = numericValue / 100;
        
        if (percentageValue <= 100) {
            setNewPersonReserve(percentageValue);
            setDisplayReserve(
                new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(percentageValue)
            );
        }
    };

    const handleAddPerson = async () => {
        if (newPersonName.trim()) {
            await addPerson(newPersonName, newPersonSalary, newPersonReserve);
            setNewPersonName('');
            setNewPersonSalary(0);
            setNewPersonReserve(0);
            setDisplaySalary("0,00");
            setDisplayReserve("0,00");
            setShowAddForm(false);
        }
    };

    const handleCancelAdd = () => {
        setNewPersonName('');
        setNewPersonSalary(0);
        setNewPersonReserve(0);
        setDisplaySalary("0,00");
        setDisplayReserve("0,00");
        setShowAddForm(false);
    };

    if (isLoading || !summary) {
        return (
            <div className="card animate-pulse flex items-center justify-center p-20">
                <div className="text-gray-400">Sincronizando participantes...</div>
            </div>
        );
    }

    return (
        <div className="grid lg:grid-cols-2 gap-6">
            <section className="card flex flex-col h-full">
                <div className="flex justify-between items-center mb-6">
                    <div>
                        <h3 className="text-xl font-bold dark:text-white" data-testid="people-list-title">Participantes</h3>
                        <p className="text-sm text-gray-500">Gerencie quem divide as contas.</p>
                    </div>
                    {!showAddForm && (
                        <button
                            onClick={() => setShowAddForm(true)}
                            className="bg-blue-50 text-blue-600 px-4 py-2 rounded-xl font-medium hover:bg-blue-100 transition-colors"
                            data-testid="btn-add-person-open"
                        >
                            + Adicionar
                        </button>
                    )}
                </div>

                {showAddForm && (
                    <div className="bg-blue-50/40 border-2 border-dashed border-blue-200 rounded-3xl p-6 mb-6 animate-in slide-in-from-top duration-300">
                        <h4 className="text-sm font-bold text-blue-600 uppercase tracking-widest mb-6" data-testid="new-participant-title">Novo Participante</h4>
                        <div className="space-y-6">
                            <div className="space-y-1">
                                <label className="text-[10px] font-bold text-blue-400 uppercase tracking-widest block ml-[1px]">Nome Completo</label>
                                <input
                                    type="text"
                                    value={newPersonName}
                                    onChange={(e) => setNewPersonName(e.target.value)}
                                    placeholder="Nome da pessoa"
                                    className="w-full bg-transparent border-none outline-none p-0 text-xl font-bold text-gray-800 dark:text-white focus:ring-0"
                                    autoFocus
                                    data-testid="input-person-name"
                                />
                            </div>
                            <div className="grid grid-cols-2 gap-6">
                                <div className="space-y-1">
                                    <label className="text-[10px] font-bold text-blue-400 uppercase tracking-widest block ml-[1px]">Salário</label>
                                    <div className="flex items-center">
                                        <span className="text-gray-400 text-sm mr-2 font-medium">R$</span>
                                        <input
                                            type="text"
                                            value={displaySalary}
                                            onChange={handleSalaryChange}
                                            className="bg-transparent border-none p-0 w-full font-semibold text-gray-700 dark:text-gray-200 focus:ring-0"
                                            placeholder="0,00"
                                            data-testid="input-person-salary"
                                        />
                                    </div>
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] font-bold text-blue-400 uppercase tracking-widest block ml-[1px]">Reserva (%)</label>
                                    <div className="flex items-center">
                                        <span className="text-gray-400 text-sm mr-2 font-medium">%</span>
                                        <input
                                            type="text"
                                            inputMode="numeric"
                                            value={displayReserve}
                                            onChange={handleReserveChange}
                                            placeholder="0,00"
                                            className="bg-transparent border-none p-0 w-full font-semibold text-gray-700 dark:text-gray-200 focus:ring-0"
                                            data-testid="input-person-reserve"
                                        />
                                    </div>
                                </div>
                            </div>
                            <div className="flex gap-2 pt-2">
                                <button onClick={handleAddPerson} className="flex-1 bg-blue-600 text-white py-3 rounded-xl font-bold hover:bg-blue-700 transition-colors shadow-lg shadow-blue-200 dark:shadow-none" data-testid="btn-save-person">Salvar Participante</button>
                                <button onClick={handleCancelAdd} className="secondary px-6 rounded-xl bg-white/50 dark:bg-gray-800/50" data-testid="btn-cancel-person">Cancelar</button>
                            </div>
                        </div>
                    </div>
                )}

                <div className="space-y-4 flex-1">
                    {people.length === 0 && (
                        <div className="p-10 text-center text-gray-400 bg-gray-50/50 dark:bg-gray-800/20 rounded-2xl border-2 border-dashed border-gray-200 dark:border-gray-700">
                            Nenhum participante cadastrado. Clique em "+ Adicionar" para começar.
                        </div>
                    )}

                    {people.map((person) => (
                        <PersonCard
                            key={person.id}
                            person={person}
                            onUpdate={updatePerson}
                            onRemove={deletePerson}
                            showRemove={true}
                        />
                    ))}
                </div>

                <div className="mt-6 pt-6 border-t border-gray-100 dark:border-gray-800">
                    <div className="flex justify-between items-center bg-gray-50 dark:bg-gray-800/50 p-4 rounded-2xl">
                        <span className="text-sm font-medium text-gray-500">Salário Total Acumulado</span>
                        <span className="text-xl font-bold text-gray-900 dark:text-white" data-testid="total-salary-value">{BRL.format(summary.totalSalary)}</span>
                    </div>
                </div>
            </section>

            <section className="card flex flex-col h-full">
                <div className="mb-6">
                    <h3 className="text-xl font-bold dark:text-white">Cálculo de Proporcionalidade</h3>
                    <p className="text-sm text-gray-500">Baseado no salário e reserva de cada um.</p>
                </div>

                <div className="space-y-6 flex-1">
                    {people.length === 0 ? (
                        <div className="h-full flex items-center justify-center p-10 text-center text-gray-400 bg-gray-50/50 dark:bg-gray-800/20 rounded-2xl border-2 border-dashed border-gray-200 dark:border-gray-700 min-h-[150px]">
                            Cadastre participantes para visualizar o cálculo de proporcionalidade.
                        </div>
                    ) : (
                        <>
                            <div>
                                <h4 className="text-xs font-bold text-gray-400 uppercase tracking-widest mb-3" data-testid="distribution-bills-title">Rateio das Contas</h4>
                                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                                    {summary.peopleData.map((data) => (
                                        <div key={data.person.id} className="p-4 bg-gray-50 dark:bg-gray-800/50 rounded-2xl border border-gray-100 dark:border-gray-700/50">
                                            <div className="text-xs text-gray-500 mb-1" data-testid={`distribution-bill-person-name-${data.person.name}`}>{data.person.name}</div>
                                            <div className="text-lg font-bold text-gray-900 dark:text-white" data-testid={`distribution-bill-percentage-${data.person.name}`}> {PC.format(data.percentage)}%</div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                            <div>
                                <h4 className="text-xs font-bold text-gray-400 uppercase tracking-widest mb-3">Valor Reservado</h4>
                                <div className="space-y-3">
                                    {summary.peopleData.map((data) => (
                                        <div key={data.person.id} className="flex justify-between items-center p-4 bg-white dark:bg-gray-800 border border-gray-100 dark:border-gray-700 rounded-2xl shadow-sm">
                                            <span className="font-medium text-gray-700 dark:text-gray-300">{data.person.name}</span>
                                            <div className="text-right">
                                                <div className="font-bold text-gray-900 dark:text-white">{BRL.format(data.reserveAmount)}</div>
                                                <div className="text-[10px] text-gray-400">{data.person.reservePercentage}% do salário</div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </>
                    )}
                </div>

                <div className="mt-6 pt-6 border-t border-gray-100 dark:border-gray-800">
                    <div className="flex justify-between items-center p-4">
                        <span className="text-sm font-medium text-gray-500">Total Reservado</span>
                        <span className="text-lg font-bold text-blue-600 dark:text-blue-400" data-testid="total-reserve-value">
                            {BRL.format(summary.totalWithReserve - summary.totalBills)}
                        </span>
                    </div>
                </div>
            </section>
        </div>
    );
};