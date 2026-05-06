import { createContext, useContext, useEffect, useState, type ReactNode } from "react";
import type { Bill } from "../types/Bill";
import type { CalculationSummary } from "../types/calculation";
import type { Person } from "../types/Person";
import { api } from "../services/api";
import React from "react";


interface AppContextType {
    people: Person[];
    bills: Bill[];
    summary: CalculationSummary | null;
    isLoading: boolean;
    error: string | null;

    refreshData: () => Promise<void>;
    addPerson: (name: string) => Promise<void>;
    updatePerson: (id: number, updates: Partial<Person>) => Promise<void>;
    deletePerson: (id: number) => Promise<void>;

    addBill: (description?: string) => Promise<void>;
    updateBill: (id: number, updates: Partial<Bill>) => Promise<void>;
    deleteBill: (id: number) => Promise<void>;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

export const useAppContext = () => {
    const context = useContext(AppContext);
    if (!context) throw new Error("useAppContext must be used within AppContextProvider")
    return context;
}

export const AppProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [people, setPeople] = useState<Person[]>([]);
    const [bills, setBills] = useState<Bill[]>([]);
    const [summary, setSummary] = useState<CalculationSummary | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const refreshData = async (silent = false) => {
        if (!silent) setIsLoading(true);
        try {
            const [peopleData, billsData, summaryData] = await Promise.all([
                api.getPeople(),
                api.getBills(),
                api.getSummary()
            ]);
            setPeople(peopleData);
            setBills(billsData);
            setSummary(summaryData);
            setError(null);
        } catch (err) {
            setError("Falha ao sincronizar com o servidor");
            console.error(err)
        } finally {
            if (!silent) setIsLoading(false)
        }
    };

    useEffect(() => {
        refreshData();
    }, []);

    const addPerson = async (name: string, salary: number, reserve: number) => {
        await api.addPerson(name, salary, reserve);
        await refreshData(true);
    };

    const updatePerson = async (id: number, updates: Partial<Person>) => {
        await api.updatePerson(id, updates);
        await refreshData(true);
    };

    const deletePerson = async (id: number) => {
        await api.deletePerson(id);
        await refreshData(true);
    };

    const addBill = async (description?: string) => {
        await api.addBill(description || "Nova Conta");
        await refreshData(true);
    };

    const updateBill = async (id: number, updates: Partial<Bill>) => {
        await api.updateBill(id, updates);
        await refreshData(true);
    };

    const deleteBill = async (id: number) => {
        await api.deleteBill(id);
        await refreshData(true);
    };

    const value = {
        people,
        bills,
        summary,
        isLoading,
        error,
        refreshData,
        addPerson,
        updatePerson,
        deletePerson,
        addBill,
        updateBill,
        deleteBill
    };

    return <AppContext.Provider value={value}>{children}</AppContext.Provider>;
} 