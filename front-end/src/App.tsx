import { BillsTable } from "./component/bills/billTable";
import { PeopleList } from "./component/people/peopleList";
import { Summary } from "./component/summary";
import { ThemeToggle } from "./component/ThemeToggle";
import { AppProvider, useAppContext } from "./context/AppContext";
import { ThemeProvider } from "./context/ThemeContext";
import React from "react";

const AppContent: React.FC = () => {
  const { error } = useAppContext();

  return (
    <div className="min-h-screen bg-[#f6f7fb] dark:bg-[#111827] p-5 transition-colors duration-300">
      <div className="max-w-[1400px] mx-auto relative">
        <div className="flex justify-between items-start mb-6">
          <div>
            <h1 className="text-2xl md:text-3xl font-bold m-0 mb-1 dark:text-white">
              Calculadora de Contas Proporcionais
            </h1>
          </div>
          <ThemeToggle />
        </div>

        {error && (
          <div className="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6 rounded-r-xl animate-bounce">
            <p className="font-bold">Atenção</p>
            <p>{error}</p>
          </div>
        )}

        <div className="space-y-6">
          <Summary />
          <PeopleList />
          <BillsTable />
        </div>

        <div className="mt-12 py-6 border-t border-[#e5e7eb] dark:border-[#374151] text-xs text-[#6b7280] dark:text-[#9ca3af] text-center">
          Os dados são processados e armazenados de forma segura no banco de dados.
          <br />
          Sincronização em tempo real ativa.
        </div>
      </div>
    </div>
  );
};

export default function App() {
  return (
    <ThemeProvider>
      <AppProvider>
        <AppContent />
      </AppProvider>
    </ThemeProvider>
  );
}