import React from "react";
import type { Bill } from "../../types/Bill";
import type { PersonCalculation } from "../../types/Person";


interface BillRowProps {
    bill: Bill;
    calculations: PersonCalculation[];
    onUpdate: (id: string, updates: Partial<Bill>) => void;
    onRemove: (id: string) => void;
}

export const BillRow: React.FC<BillRowProps> = ({
    bill,
    calculations,
    onUpdate,
    onRemove
}) => {
    const BRL = new Intl.NumberFormat('pt-br', { style: 'currency', currency: 'BRL' });
    const PC = new Intl.NumberFormat('pt-br', {maximumFractionDigits:1, minimumFractionDigits:1});

    return(
        <tr>
            <td>
                <input 
                type="text" 
                value={bill.description}
                onChange={(e) => onUpdate(bill.id, { description: e.target.value })} 
                placeholder="Ex: Aluguel"/>
            </td>
            <td className="text-right">
                <input 
                type="number"
                value={bill.totalAmount || '' }
                onChange={(e) => onUpdate(bill.id, { totalAmount: Number(e.target.value) })} 
                className="text-right w-24" />
            </td>
            {calculations.map((calc) => (
                <React.Fragment key={calc.id}>
                    <td className="text-right">{PC.format(calc.percentage * 100)}</td>
                    <td className="text-right">{BRL.format(bill.totalAmount * calc.percentage)}</td>
                </React.Fragment>
            ))}
            <td className="text-right">
                <button className="secondary" onClick={() => onRemove(bill.id)}>X</button>
            </td>
        </tr>
    );
}