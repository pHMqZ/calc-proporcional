import React from "react";
import { type Bill } from "../../types/Bill";
import { type PersonData } from "../../types/Person";


interface BillRowProps {
    bill: Bill;
    peopleData: PersonData[];
    onUpdate: (id: number, updates: Partial<Bill>) => Promise<void>;
    onRemove: (id: number) => Promise<void>;
}

export const BillRow: React.FC<BillRowProps> = ({
    bill,
    peopleData,
    onUpdate,
    onRemove
}) => {
    const BRL = new Intl.NumberFormat('pt-br', { style: 'currency', currency: 'BRL' });

    const [localDescription, setLocalDescription] = React.useState(bill.description);
    const [localAmount, setLocalAmount] = React.useState(bill.totalAmount * 100);

    React.useEffect(() => {
        setLocalDescription(bill.description);
        setLocalAmount(bill.totalAmount * 100);
    }, [bill.description, bill.totalAmount]);

    const handleSave = () => {
        const numericAmount = localAmount / 100;
        if (localDescription !== bill.description || numericAmount !== bill.totalAmount) {
            onUpdate(bill.id, {
                description: localDescription,
                totalAmount: numericAmount
            });
        }
    };


    return (
        <tr className="hover:bg-gray-50 dark:hover:bg-gray-800/40 transition-colors group">
            <td className="p-4">
                <input
                    type="text"
                    value={localDescription}
                    onChange={(e) => setLocalDescription(e.target.value)}
                    onBlur={handleSave}
                    className="bg-transparent border-none focus:ring-0 w-full font-medium text-gray-700 dark:text-gray-200 px-2 py-1"
                    placeholder="Ex: Aluguel"
                    data-testid={`input-bill-description-${bill.description}`} />
            </td>
            <td className="p-4">
                <div className="flex items-center justify-end">
                    <span className="text-gray-400 text-xs mr-2 font-medium">R$</span>
                    <input
                        type="text"
                        value={new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(localAmount / 100)}
                        onChange={(e) => setLocalAmount(Number(e.target.value.replace(/\D/g, '')))}
                        onBlur={handleSave}
                        className="bg-transparent border-none text-right focus:ring-0 w-full font-bold text-gray-900 dark:text-white px-2 py-1"
                        data-testid={`input-bill-amount-${bill.description}`} />
                </div>
            </td>
            {peopleData.map((data) => {
                const distribution = data.billDistributions.find(d => d.billId === bill.id);

                return (
                    <td key={data.person.id} className="p-4 text-right font-medium text-gray-900 dark:text-white">
                        {distribution ? BRL.format(distribution.amount) : BRL.format(0)}
                    </td>
                );
            })}
            <td className="p-4 text-right">
                <button
                    className="text-gray-300 hover:text-red-700 hover:bg-red-50 dark:hover:bg-red-900/20 transition-all p-2 rounded-lg opacity-0 group-hover:opacity-100"
                    onClick={() => onRemove(bill.id)}
                    title="Remover conta"
                    data-testid={`btn-remove-bill-${bill.description}`}
                >
                    ✕
                </button>
            </td>
        </tr>
    );
}