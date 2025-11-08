import { useState } from "react";
import { useAppContext } from "../../context/AppContext"
import { PersonCard } from "./personCard";

export const PeopleList: React.FC = () =>{
    const { people, addPerson, updatePerson, removePerson, totalSalary, calculations } = useAppContext();
    const [newPersonName, setNewPersonName] = useState("");
    const [showAddForm, setShowAddForm] = useState(false);

    const BRL = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL",
    });

    const PC = new Intl.NumberFormat("pt-BR", {
        style: "percent",
        minimumFractionDigits: 1,
        maximumFractionDigits: 1,
    });

   const handleAddPerson = () => {
        if (newPersonName.trim()) {
            addPerson(newPersonName);
            setNewPersonName('');
            setShowAddForm(false);
        } else {
            alert("Por favor, insira um nome válido.");
        }
   };

   const handleCancel = () => {
        setNewPersonName('');
        setShowAddForm(false);
   };

   const totalReserve = calculations.reduce((acc, calc) => acc + calc.reserveAmount, 0);


   return (
        <div className="grid md:grid-cols-2 gap-3.5">
            <section className="card">
                <div className="flex justify-between items-center mb-4">
                    <h3 className="text-lg font-semibold">Pessoas</h3>
                    { !showAddForm ? (
                        <button onClick={() => setShowAddForm(true)}>
                        + Adicionar Pessoa</button>
                    ) : null }
                </div>

                { showAddForm && (
                    <div className="card mb-4 bg-[#f9fafb] border-2 border-dashed border-[#c7d2fe]">
                        <h4 className="text-sm font-semibold mb-3 text-[#6b7280]">Nova Pessoa</h4>
                        <div className="flex gap-2 items-end">
                            <div className="flex-1">
                                <label className="text-xs text-[#6b7280] block mb-1">Nome</label>
                                <input
                                    type="text"
                                    value={newPersonName}
                                    onChange={(e) => setNewPersonName(e.target.value)}
                                    onKeyPress={(e) => e.key === 'Enter'&& handleAddPerson()}
                                    placeholder="Digite o nome"
                                    autoFocus/>
                            </div>
                            <button onClick={handleAddPerson}>Adicionar</button>
                            <button onClick={handleCancel} className="secondary">Cancelar</button>
                        </div>
                    </div>
                )}

                <div className="space-y-3 mb-4">
                    {people.map((person) =>(
                        <PersonCard 
                            key={person.id}
                            person={person}
                            onUpdate={updatePerson}
                            onRemove={removePerson}
                            showRemove={people.length > 1}
                        />
                    ))}
                </div>

                {people.length === 0 &&(
                    <div className="text-center py-8 text-[#6b7280] text-sm">
                        Nenhum participante adicionado ainda.
                    </div>
                )}

               <div className="border-t border-[#e5e7eb] pt-4 mb-4">
                <div className="kpi-item">
                    <div className="text-[#6b7280] text-xs">Salário Total</div>
                    <div className="font-bold text-lg">{BRL.format(totalSalary)}</div>
                </div>
               </div>

               <div className="border-t border-[#e5e7eb] pt-4 mt-4">
                <h4 className="text-sm font-semibold mb-3 text-[#6b7280]">Participaçao nos custos</h4>
                <div className="grid grid-cols-2 gap-2">
                    {calculations.map((calc) => (
                        <div className="kpi-item">
                            <div className="text-[#6b7280] text-xs">{calc.name}</div>
                            <div className="font-bold text-base">{PC.format(calc.percentage)}</div>
                        </div>
                    ))}
                </div>
               </div>
            </section>
            <section className="card">
                <h3 className="text-lg font-semibold mb-4">Reserva</h3>

                <div className="sapce-y-2 mb-4">
                    {calculations.map((calc) => (
                        <div key={calc.id} className="pill justify-between w-full">
                            <strong>{calc.name}</strong>
                            <span>
                                {BRL.format(calc.reserveAmount)} ({calc.reservePercentage}%)
                            </span>
                        </div>
                    ))}
                </div>

                {calculations.length === 0 && (
                    <div className="text-center py-8 text-[#6b7280] text-sm">
                        Nenhum participante adicionado ainda.
                    </div>
                )}

                <div className="border-t border-[#e5e7eb] pt-4 mt-4">
                    <div className="kpi-item">
                        <div className="text-[#6b7280] text-xs">Total da Reserva</div>
                        <div className="font-bold">{BRL.format(totalReserve)}</div>
                    </div>
                </div>

            </section>
        </div>
   )
}