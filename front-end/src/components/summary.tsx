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
        return (
            <div className="card animate-pulse flex flex-col gap-4">
                <div className="h-6 w-32 bg-gray-100 rounded"></div>
                <div className="grid md:grid-cols-3 gap-4">
                    <div className="h-24 bg-gray-50 rounded-2xl"></div>
                    <div className="h-24 bg-gray-50 rounded-2xl"></div>
                    <div className="h-24 bg-gray-50 rounded-2xl"></div>
                </div>
            </div>
        );
    }

    return (
        <>
            <section className="card bg-gradient-to-br from-white to-gray-50 dark:from-gray-800 dark:to-gray-900">
                <div className="flex justify-between items-center mb-6">
                    <div>
                        <h3 className="text-xl font-bold dark:text-white">Resumo Financeiro</h3>
                        <p className="text-sm text-gray-500">Divisão final após rateio proporcional.</p>
                    </div>
                    <button
                        onClick={() => setShowImageModal(true)}
                        className="px-4 py-2 bg-blue-600 text-white rounded-xl hover:bg-blue-700 transition-colors disabled:opacity-50"
                    >
                        <span>📸</span> Gerar Imagem
                    </button>
                </div>

                <div className="grid lg:grid-cols-2 gap-8">

                    <div className="space-y-4">
                        <h4 className="text-xs font-bold text-gray-400 uppercase tracking-widest">A PAGAR (CONTA + RESERVA)</h4>
                        <div className="grid sm:grid-cols-2 gap-4">
                            {summary.peopleData.map((data) => (
                                <div key={data.person.id} className="p-5 bg-white dark:bg-gray-800 border border-gray-100 dark:border-gray-700 rounded-2xl shadow-sm">
                                    <div className="text-sm text-gray-500 mb-1">{data.person.name}</div>
                                    <div className="text-2xl font-black text-gray-900 dark:text-white">
                                        {BRL.format(data.totalToPay)}
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>


                    <div className="space-y-4">
                        <h4 className="text-xs font-bold text-gray-400 uppercase tracking-widest">SALDO RESTANTE</h4>
                        <div className="grid sm:grid-cols-2 gap-4">
                            {summary.peopleData.map((data) => (
                                <div key={data.person.id} className={`p-5 rounded-2xl shadow-sm border ${data.remainingSalary < 0
                                    ? 'bg-red-50 dark:bg-red-900/20 border-red-100 dark:border-red-800'
                                    : 'bg-green-50 dark:bg-green-900/20 border-green-100 dark:border-green-800'
                                    }`}>
                                    <div className={`text-sm mb-1 font-medium ${data.remainingSalary < 0 ? 'text-red-600 dark:text-red-400' : 'text-green-600 dark:text-green-400'
                                        }`}>{data.person.name}</div>
                                    <div className={`text-2xl font-black ${data.remainingSalary < 0 ? 'text-red-700 dark:text-red-300' : 'text-green-700 dark:text-green-300'
                                        }`}>
                                        {BRL.format(data.remainingSalary)}
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>

                <div className="mt-8 pt-6 border-t border-gray-100 dark:border-gray-800 grid sm:grid-cols-3 gap-6">
                    <div className="text-center sm:text-left">
                        <div className="text-[10px] font-bold text-gray-400 uppercase tracking-widest">Total de Contas</div>
                        <div className="text-xl font-bold text-gray-800 dark:text-gray-200">{BRL.format(summary.totalBills)}</div>
                    </div>
                    <div className="text-center sm:text-left">
                        <div className="text-[10px] font-bold text-gray-400 uppercase tracking-widest">Total Reservado</div>
                        <div className="text-xl font-bold text-blue-600">{BRL.format(summary.totalWithReserve - summary.totalBills)}</div>
                    </div>
                    <div className="text-center sm:text-left">
                        <div className="text-[10px] font-bold text-gray-400 uppercase tracking-widest">Total Geral</div>
                        <div className="text-xl font-bold text-gray-900 dark:text-white">{BRL.format(summary.totalWithReserve)}</div>
                    </div>
                </div>
            </section>

            {showImageModal && (
                <ImageGenerator onClose={() => setShowImageModal(false)} />
            )}
        </>
    );
};