import React from "react";
import { useAppContext } from "../../context/AppContext";
import { BillRow } from "./billsRow";


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

    return(
        <section className="card">
            <div className="flex justify-between items-center mb-5 flex-wrap gap-2">
                <div>
                    <h3 className="text-xl font-bold dark:text-white">Contas Compartilhadas</h3>
                    <p className="text-sm text-gray-500">Rateio automático baseado na participação de cada um.</p>
                </div>
                <div className="flex gap-2 flex-wrap">
                    <button 
                        onClick={handleAddBill} 
                        disabled={isAdding}
                        className="px-4 py-2 bg-blue-600 text-white rounded-xl hover:bg-blue-700 transition-colors disabled:opacity-50"
                    > 
                        {isAdding ? "Adicionando..." : "+ Adicionar conta"}
                    </button>
                </div>
            </div>

            <div className="overflow-x-auto rounded-xl border border-gray-100 dark:border-gray-800">
                <table className="w-full text-left border-collapse">
                    <thead className="bg-gray-50 dark:bg-gray-800/50">
                        <tr>
                            <th className="p-4 font-semibold text-gray-600 dark:text-gray-300">Conta</th>
                            <th className="p-4 text-right font-semibold text-gray-600 dark:text-gray-300">Valor Total</th>
                            {peopleData.map((data) => (
                                <th key={data.person.id} className="p-4 text-right font-semibold text-gray-600 dark:text-gray-300">
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
                                <td className="p-4 text-right font-bold text-blue-600 dark:text-blue-400">
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
        </section>
    );
};