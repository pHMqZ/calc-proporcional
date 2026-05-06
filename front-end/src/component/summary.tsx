import type React from "react";
import { useAppContext } from "../context/AppContext";
import { useState } from "react";
import { ImageGenerator } from "./imageGenerator";

export const Summary: React.FC = () => {
    const { summary, isLoading } = useAppContext();
    const [showImageModal, setShowImageModal] = useState(false);

    const BRL = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL",
    });

    if (isLoading || !summary) {
        return <section className="card animate-pulse">Carregando resumo financeiro...</section>;
    }

    return (
        <>
            <section className="card">
                <h3 className="text-lg font-semibold mb-3">Resumo</h3>
                <div className="mb-4">
                    <h4 className="text-sm font-semibold mb-3 text-[#6b7280]">Conta + Reserva</h4>
                    <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-3">
                        {summary.peopleData.map((calc) => (
                            <div key={calc.person.id}>
                                <div className="text-[#6b7280] text-sm font-medium mb-1">{calc.person.name}</div>
                                <div className="text-2xl font-bold">{BRL.format(calc.totalToPay)}</div>
                            </div>
                        ))}
                    </div>
                </div>

                <div className="mb-4">
                    <h4 className="text-sm font-semibold mb-3 text-[#6b7280]">Valor restante</h4>
                    <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-3">
                        {summary.peopleData.map((calc) => (
                            <div key={calc.person.id}>
                                <div className="text-[#6b7280] text-sm font-medium mb-1"><strong>{calc.person.name}</strong></div>
                                <div className="text-2xl font-bold text-[#16a34a]">{BRL.format(calc.remainingSalary)}</div>
                            </div>
                        ))}
                    </div>
                </div>

                <button onClick={() => setShowImageModal(true)} className="mt-3">Gerar imagem</button>
                <div className="text-xs text-[#6b7280] mt-2">
                    A imagem contem o resumo e a lista de contas rateadas.
                </div>
            </section>

            {showImageModal && (
                <ImageGenerator onClose={() => setShowImageModal(false)} />
            )}
        </>
    );
};