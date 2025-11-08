import { BillsTable } from "./component/bills/billTable";
import { PeopleList } from "./component/people/peopleList";
import { Summary } from "./component/summary";
import { AppProvider } from "./context/AppContext";

export default function App(){
  return (
    <AppProvider>
      <div className="min-h-screen bg-[#f6f7fb] p-5">
        <div className="max-w-[1400px] mx-auto space-y-3.5">
          <h1 className="text-2xl md:text-3xl font-bold m-0 mb-1">
            Calculadora de contas compartilhadas
          </h1>
          <div className="text-[#6b7280] mb-3">
            Distribui contas e reserva proporcionalmente ao salário dos participantes.
          </div>

          <PeopleList />
          <BillsTable />
          <Summary />

          <div className="text-xs text-[#6b7280]">
            Basta preencher os salários, porcentagens da reserva e contas e tudo será atualizado automaticamente
          </div>
        </div>     
      </div>
    </AppProvider>
  );
}