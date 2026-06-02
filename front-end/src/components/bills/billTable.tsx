import React from "react";
import { useAppContext } from "../../context/AppContext";
import { BillRow } from "./billsRow";
import { BillCard } from "./billCard";


export const BillsTable: React.FC = () => {
    const {
        bills,
        summary,
        addBill,
        updateBill,
        deleteBill,
    } = useAppContext();

    const BRL = new Intl.NumberFormat('pt-br', { style: 'currency', currency: 'BRL' });
    const PC = new Intl.NumberFormat('pt-br', { maximumFractionDigits: 1, minimumFractionDigits: 1 });

    if (!summary) {
        return (
            <section className="card animate-pulse">
                <div className="h-48 bg-gray-100 dark:bg-gray-800 rounded-xl flex items-center justify-center">
                    Carregando tabela de contas...
                </div>
            </section>
        );
    }

    const { peopleData, totalBills } = summary;

    const [isAdding, setIsAdding] = React.useState(false);

    const handleAddBill = async () => {
        setIsAdding(true);
        try {
            await addBill();
        } finally {
            setIsAdding(false);
        }
    };

    return (
        <section className="card">
            <div className="flex justify-between items-center mb-5 flex-wrap gap-2">
                <div>
                    <h3 className="text-xl font-bold dark:text-white" data-testid="bills-list-title">Contas Compartilhadas</h3>
                    <p className="text-sm text-gray-500">Rateio automático baseado na participação de cada um.</p>
                </div>
                <div className="flex gap-2 flex-wrap">
                    <button
                        onClick={handleAddBill}
                        disabled={isAdding}
                        className="px-4 py-2 bg-blue-600 text-white rounded-xl hover:bg-blue-700 transition-colors disabled:opacity-50"
                        data-testid="btn-add-bill"
                    >
                        {isAdding ? "Adicionando..." : "+ Adicionar conta"}
                    </button>
                </div>
            </div>

            <div className="hidden md:block overflow-x-auto rounded-xl border border-gray-100 dark:border-gray-800">
                <table className="text-left border-collapse w-full">
                    <thead className="bg-gray-50 dark:bg-gray-800/50">
                        <tr>
                            <th className="p-4 font-semibold text-gray-600 dark:text-gray-300 whitespace-nowrap min-w-[150px]">Conta</th>
                            <th className="p-4 text-right font-semibold text-gray-600 dark:text-gray-300 whitespace-nowrap min-w-[150px]">Valor Total</th>
                            {peopleData.map((data) => (
                                <th key={data.person.id} className="p-4 text-right font-semibold text-gray-600 dark:text-gray-300 whitespace-nowrap ">
                                    {data.person.name} ({PC.format(data.percentage)}%)
                                </th>
                            ))}
                            <th className="p-4"></th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-100 dark:divide-gray-800">
                        {bills.map((bill) => (
                            <BillRow
                                key={bill.id}
                                bill={bill}
                                peopleData={peopleData}
                                onUpdate={updateBill}
                                onRemove={deleteBill}
                            />
                        ))}
                        {bills.length === 0 && (
                            <tr>
                                <td colSpan={3 + peopleData.length} className="p-10 text-center text-gray-400">
                                    Nenhuma conta cadastrada. Clique em "Adicionar conta" para começar.
                                </td>
                            </tr>
                        )}
                    </tbody>
                    {bills.length > 0 && (
                        <tfoot className="bg-gray-50 dark:bg-gray-800/30">
                            <tr>
                                <td className="p-4 text-right font-bold dark:text-white">
                                    TOTAL
                                </td>
                                <td className="p-4 text-right font-bold text-blue-600 dark:text-blue-400" data-testid="total-bills-value">
                                    {BRL.format(totalBills)}
                                </td>
                                {peopleData.map((data) => (
                                    <td key={data.person.id} className="p-4 text-right font-bold text-gray-900 dark:text-white">
                                        {BRL.format(data.billsTotal)}
                                    </td>
                                ))}
                                <td className="p-4"></td>
                            </tr>
                        </tfoot>
                    )}
                </table>
            </div>

            <div className="block md:hidden space-y-4">
                {bills.map((bill) => (
                    <BillCard
                        key={bill.id}
                        bill={bill}
                        peopleData={peopleData}
                        onUpdate={updateBill}
                        onRemove={deleteBill}
                    />
                ))}
                {bills.length === 0 && (
                    <div className="p-10 text-center text-gray-400 bg-gray-50/50 dark:bg-gray-800/20 rounded-2xl border-2 border-dashed border-gray-200 dark:border-gray-700">
                        Nenhuma conta cadastrada. Clique em "+ Adicionar conta" para começar.
                    </div>
                )}
                {/* Resumo Geral Acumulado para Mobile */}
                {bills.length > 0 && (
                    <div className="bg-gray-50 dark:bg-gray-800/40 p-4 rounded-2xl border border-gray-100 dark:border-gray-700/60 space-y-3">
                        <div className="flex justify-between items-center font-bold text-sm">
                            <span className="text-gray-500">VALOR TOTAL DE CONTAS</span>
                            <span className="text-blue-600 dark:text-blue-400">
                                {BRL.format(totalBills)}
                            </span>
                        </div>
                        <div className="border-t border-gray-200 dark:border-gray-700 pt-2 space-y-2 text-xs">
                            <span className="font-bold text-gray-400 uppercase tracking-widest text-[9px] block">Rateio Total</span>
                            {peopleData.map((data) => (
                                <div key={data.person.id} className="flex justify-between items-center">
                                    <span className="text-gray-600 dark:text-gray-300">{data.person.name}</span>
                                    <span className="font-bold text-gray-900 dark:text-white">
                                        {BRL.format(data.billsTotal)}
                                    </span>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </section>
    );
};