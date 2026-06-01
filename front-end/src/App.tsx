import { BillsTable } from "./components/bills/billTable";
import { PeopleList } from "./components/people/peopleList";
import { Summary } from "./components/summary";
import { ThemeToggle } from "./components/ThemeToggle";
import { AppProvider, useAppContext } from "./context/AppContext";
import { ThemeProvider } from "./context/ThemeContext";
import React from "react";

const AppContent: React.FC = () => {
  const { error } = useAppContext();
  const [visibleError, setVisibleError] = React.useState<string | null>(null);

  React.useEffect(() => {
    if (error) {
      setVisibleError(error);
    } else {
      setVisibleError(null);
    }
  }, [error]);

  return (
    <div className="min-h-screen bg-[#f6f7fb] dark:bg-[#111827] p-5 transition-colors duration-300">
      <div className="max-w-[1400px] mx-auto relative">
        <div className="flex justify-between items-center mb-12">
          <div>
            <h1 className="text-2xl md:text-3xl font-bold m-0 mb-1 dark:text-white">
              Calculadora de Contas Proporcionais
            </h1>
          </div>
          <ThemeToggle />
        </div>

        {visibleError && (
          <div 
            className="fixed top-6 right-6 z-50 max-w-sm w-full bg-white dark:bg-gray-800 border-l-4 border-red-500 rounded-r-2xl shadow-xl shadow-gray-200/50 dark:shadow-none p-4 flex items-start gap-3 animate-in slide-in-from-top-4 duration-300"
            role="alert"
          >
            <div className="flex-1">
              <p className="font-bold text-sm text-red-600 dark:text-red-400">Atenção</p>
              <p className="text-xs text-gray-500 dark:text-gray-300 mt-1">{visibleError}</p>
            </div>
            <button 
              onClick={() => setVisibleError(null)}
              className="text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 transition-colors p-1"
              aria-label="Fechar notificação"
            >
              ✕
            </button>
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