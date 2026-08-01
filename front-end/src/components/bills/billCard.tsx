import React from 'react';
import type { Bill } from "../../types/Bill";
import type { PersonData } from '../../types/Person';

interface BillCardProps {
    bill: Bill;
    peopleData: PersonData[];
    onUpdate: (id: string, updates: Partial<Bill>) => Promise<void>;
    onRemove: (id: string) => Promise<void>;
}

export const BillCard: React.FC<BillCardProps> = ({
    bill,
    peopleData,
    onUpdate,
    onRemove
}) => {
    const BRL = new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    });

    const [localDescription, setLocalDescription] = React.useState(bill.description);
    const [localAmount, setLocalAmount] = React.useState(bill.totalAmount * 100);

    const [displayAmount, setDisplayAmount] = React.useState(
        new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(bill.totalAmount)
    );

    React.useEffect(() => {
        setLocalDescription(bill.description);
        setLocalAmount(bill.totalAmount * 100);
        setDisplayAmount(
            new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(bill.totalAmount)
        );
    }, [bill.description, bill.totalAmount]);


    const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const rawValue = e.target.value;

        if (rawValue === "") {
            setDisplayAmount("");
            setLocalAmount(0);
            return;
        }

        const digits = rawValue.replace(/\D/g, '');
        if (digits === "") {
            setDisplayAmount("");
            setLocalAmount(0);
            return;
        }

        const numericValue = Number(digits);
        setLocalAmount(numericValue);

        setDisplayAmount(
            new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(numericValue / 100)
        );
    };

    const handleSave = () => {
        const numericAmount = localAmount / 100;
        if (localDescription !== bill.description || numericAmount !== bill.totalAmount) {
            onUpdate(bill.id, {
                description: localDescription,
                totalAmount: numericAmount
            });
        }


        setDisplayAmount(
            new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(numericAmount)
        );
    };

    return (
        <div className="bg-white dark:bg-gray-800 border border-gray-100 dark:border-gray-700 rounded-2xl p-5 shadow-sm space-y-4">


            <div className="flex justify-between items-center gap-4">


                <div className="flex-1 space-y-1">
                    <label className="text-[10px] font-bold text-gray-400 dark:text-gray-500 uppercase tracking-widest block ml-[2px]">
                        Conta
                    </label>
                    <input
                        type="text"
                        value={localDescription}
                        onChange={(e) => setLocalDescription(e.target.value)}
                        onBlur={handleSave}
                        className="bg-transparent border-none focus:ring-0 w-full font-bold text-gray-855 dark:text-white p-0 text-base"
                        placeholder="Ex: Aluguel"
                        data-testid={`input-bill-description-mobile-${bill.description}`}
                    />
                </div>

                <div className="flex-1 space-y-1">
                    <label className="text-[10px] font-bold text-gray-400 dark:text-gray-500 uppercase tracking-widest block ml-[2px]">
                        Valor Total
                    </label>
                    <div className="flex items-center">
                        <span className="text-gray-400 dark:text-gray-500 mr-1 font-medium text-sm">R$</span>
                        <input
                            type="text"
                            value={displayAmount}
                            onChange={handleAmountChange}
                            onBlur={handleSave}
                            className="bg-transparent border-none focus:ring-0 w-full font-bold text-gray-900 dark:text-white p-0 text-left"
                            placeholder="0,00"
                            data-testid={`input-bill-value-mobile-${bill.description}`}
                        />
                    </div>
                </div>


                <button
                    onClick={() => onRemove(bill.id)}
                    className="flex-shrink-0 flex items-center justify-center font-bold text-sm bg-[#4f46e5] text-white hover:bg-[#4338ca] transition-all cursor-pointer rounded-xl h-11 px-3 mt-4"
                    title="Remover conta"
                    data-testid={`btn-remove-bill-mobile-${bill.description}`}
                >
                    ✕
                </button>
            </div>


            <div className="border-t border-gray-100 dark:border-gray-700 pt-3">
                <label className="text-[10px] font-bold text-gray-400 dark:text-gray-500 uppercase tracking-widest block mb-3 ml-[2px]">
                    Rateio Proporcional
                </label>
                <div className="grid grid-cols-2 gap-2 text-xs">
                    {peopleData.map((data) => {
                        const distribution = data.billDistributions.find(d => d.billId === bill.id);

                        return (
                            <div className="bg-gray-50 dark:bg-gray-900/50 p-2.5 rounded-xl flex flex-col" key={data.person.id}>
                                <span className="text-gray-500 dark:text-gray-400 truncate font-medium">{data.person.name}</span>
                                <span className="font-bold text-gray-800 dark:text-gray-200 mt-0.5">
                                    {distribution ? BRL.format(distribution.amount) : BRL.format(0)}
                                </span>
                            </div>
                        );
                    })}
                </div>
            </div>

        </div>
    );
};