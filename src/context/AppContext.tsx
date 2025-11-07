import React, { createContext, useContext, useState, ReactNode } from "react";
import type { Bill, BillDistribution } from "../types/Bill";
import type { Person, PersonCalculation } from "../types/Person";
import { CalculationService } from "../services/calculation";

interface AppContextType {
    people: Person[];
    bills: Bill[];
    calculations: PersonCalculation[];
    distribuitions: BillDistribution[];
    totalSalary: number;
    totalBills: number;
    totalReserve: number;

    addPerson: (name: string) => void;
    updatePerson: (id: string, updates: Partial<Person>) => void;
    removePerson: (id: string) => void;
    
    addBill: (description?: string) => void;
    updateBill: (id: string, updates: Partial<Bill>) => void;
    removeBill: (id: string) => void;
    clearBills: () => void;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

export const useAppContext = () => {
    const context = useContext(AppContext);
    if (!context) {
        throw new Error("useAppContext must be used within AppContextProvider");
    }
    return context;
};

interface AppProviderProps {
    children: ReactNode;
}

export const AppProvider: React.FC<AppProviderProps> = ({ children }) => {
    const [people, setPeople] = useState<Person[]>([]);
    const [bills, setBills] = useState<Bill[]>([]);

    const totalSalary = CalculationService.calculateTotalSalary(people);
    const totalBills = CalculationService.calculateTotalBills(bills);
    const distribuitions = CalculationService.calculateBillDistribution(
        bills, 
        people, 
        totalSalary
    );
    const calculations = CalculationService.calculateAllPeopleData(people,bills);
    const totalReserve = calculations.reduce((sum, p) => sum + p.reserveAmount, 0);

    const addPerson = (name: string) => {
        const newPerson: Person = {
            id: Date.now().toString(),
            name: name,
            salary: 0,
            reservePercentage: 0
        };
        setPeople([...people, newPerson]);
    };

    const updatePerson = (id: string, updates: Partial<Person>) => {
        setPeople(people.map(p => p.id === id ? { ...p, ...updates } : p));
    };

    const removePerson = (id: string) => {
        if (people.length <= 0){
            alert("Sem pessoas para remover.");
            return;
        }
        setPeople(people.filter(p => p.id !== id));
    };

    const addBill = (description?: string) => {
        const newBill: Bill = {
            id: Date.now().toString(),
            description: description || "",
            totalAmount: 0
        };
        setBills([...bills, newBill]);
    };

    const updateBill = (id: string, updates: Partial<Bill>) => {
        setBills(bills.map(b => (b.id === id ? { ...b, ...updates } : b)));
    };

    const removeBill = (id: string) => {
        if (bills.length <= 0){
            alert("Sem contas para remover.");
            return;
        }
        setBills(bills.filter(b => b.id !== id));
    };

    const clearBills = () => {
        setBills([]);
    }


    return (
        <AppContext.Provider
            value={{
                people,
                bills,
                calculations,
                distribuitions,
                totalSalary,
                totalBills,
                totalReserve,
                addPerson,
                updatePerson,
                removePerson,
                addBill,
                updateBill,
                removeBill,
                clearBills
            }}
        >
            {children}
        </AppContext.Provider>
    );
};   