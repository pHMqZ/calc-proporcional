import { BillsTable } from "./component/bills/billTable";
import { PeopleList } from "./component/people/peopleList";
import { Summary } from "./component/summary";
import { ThemeToggle } from "./component/ThemeToggle";
import { AppProvider } from "./context/AppContext";
import { ThemeProvider } from "./context/ThemeContext";

export default function App(){
  return (
    <ThemeProvider>
      <AppProvider>
        <div className="min-h-screen bg-[#f6f7fb] dark:bg-[#111827] p-5 transition-colors duration-300">
          <ThemeToggle />
          <div className="max-w-[1400px] mx-auto space-y-6">
            <h1 className="text-2xl md:text-3xl font-bold m-0 mb-1 dark:text-white">
              Calculadora de contas compartilhadas
            </h1>
            <div className="text-[#6b7280] dark:text-[#9ca3af] mb-3">
              Distribui contas e reserva proporcionalmente ao salário dos participantes.
            </div>

            <PeopleList />
            <BillsTable />
            <Summary />

            <div className="text-xs text-[#6b7280] dark:text-[#9ca3af] text-center">
              Basta preencher os salários, porcentagens da reserva e contas e tudo será atualizado automaticamente.
              <br />
              Dados são salvos automaticamente no navegador.
            </div>
          </div>     
        </div>
      </AppProvider>
    </ThemeProvider>
  );
}