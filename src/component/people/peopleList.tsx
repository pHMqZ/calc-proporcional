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
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
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

   return (
        <section className="people-list">
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

            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-3.5 mb-4">
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

            <div className="grid md:grid-cols-3 gap-2.5 mt-4">
                <div className="kpi-item">
                    <div className="text-[#6b7280] text-sx">Salário Total</div>
                    <div className="font-bold text-lg">{BRL.format(totalSalary)}</div>
                </div>
                {calculations.map((calc) => (
                    <div key={calc.id} className="kpi-item">
                        <div className="text-[#6b7280] text-sx">% {calc.name}</div>
                        <div className="font-bold text-lg">{PC.format(calc.percentage * 100)}%</div>
                    </div>
                ))}
            </div>
        
        </section>
   )
}