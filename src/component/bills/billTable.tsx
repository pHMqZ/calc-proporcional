import React from "react";
import { useAppContext } from "../../context/AppContext";
import { BillRow } from "./billsRow";


export const BillsTable: React.FC = () => {
    const {
        bills,
        calculations,
        totalBills,
        addBill,
        updateBill,
        removeBill,
        clearBills,
    } = useAppContext();

    const BRL = new Intl.NumberFormat('pt-br', { style: 'currency', currency: 'BRL' });

    return(
        <section className="billTable">
            <div className="flex justify-between items-center mb-3 flex-wrap gap-2">
                <h3 className="text-lg font-semibold">Contas compartilhadas</h3>
                <div className="flex gap-2 flex-wrap">
                    <button onClick={() => addBill()}> + Adicionar conta</button>
                    <button className="secondary" onClick={clearBills}>Limpar</button>
                </div>
            </div>

            <div className="overflow-x-auto">
                <table>
                    <thead>
                        <tr>
                            <th>Conta</th>
                            <th className="text-right">Valor Total</th>
                            {calculations.map((calc) => (
                                <React.Fragment key={calc.id}>
                                    <th className="text-right">% de {calc.name}</th>
                                    <th className="text-right">{calc.name} (R$)</th>
                                </React.Fragment>
                            ))}
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {bills.map((bill) => (
                            <BillRow
                                key={bill.id}
                                bill={bill}
                                calculations={calculations}
                                onUpdate={updateBill}
                                onRemove={removeBill}
                            />
                        ))}
                    </tbody>
                    <tfoot>
                        <tr>
                            <td className="text-right">
                                <strong>TOTAL</strong>
                            </td>
                            <td className="text-right">{BRL.format(totalBills)}</td>
                            {calculations.map((calc) => (
                                <React.Fragment key={calc.id}>
                                    <td></td>
                                    <td className="text-right">{BRL.format(calc.billsTotal)}</td>
                                </React.Fragment>
                            ))}
                            <td></td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </section>
    );
};