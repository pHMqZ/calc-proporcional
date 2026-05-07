import type { Bill } from "../types/Bill";
import type { CalculationSummary } from "../types/Calculation";
import type { Person } from "../types/Person";

const ENDPOINT = import.meta.env.VITE_API_URL;

export const api = {
    async getSummary(): Promise<CalculationSummary> {
        const response = await fetch(`${ENDPOINT}/calculation`);
        if (!response.ok) throw new Error("Erro ao buscar resumo de cálculo");
        return response.json();
    },

    async getPeople(): Promise<Person[]> {
        const response = await fetch(`${ENDPOINT}/person`);
        if (!response.ok) throw new Error("Erro ao buscar pessoas");
        return response.json();
    },

    async addPerson(name: string, salary: number, reservePercentage: number): Promise<Person> {
        const response = await fetch(`${ENDPOINT}/person`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ 
                name, 
                salary: Number(salary) || 0, 
                reservePercentage: Number(reservePercentage) || 0 
            })
        });
        if (!response.ok) throw new Error("Erro ao adicionar pessoa");
        return response.json();
    },

    async updatePerson(id: number, updates: Partial<Person>): Promise<Person> {
        const response = await fetch(`${ENDPOINT}/person/${id}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updates)
        });
        if (!response.ok) throw new Error("Erro ao atualizar pessoa");
        return response.json();
    },

    async deletePerson(id: number): Promise<void> {
        await fetch(`${ENDPOINT}/person/${id}`, {
            method: 'DELETE',
        });
    },

    async getBills(): Promise<Bill[]> {
        const response = await fetch(`${ENDPOINT}/bill`);
        if (!response.ok) throw new Error("Erro ao buscar contas");
        return response.json();
    },

    async addBill(description: string): Promise<Bill> {
        const response = await fetch(`${ENDPOINT}/bill`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ 
                description: description || "Nova Conta", 
                totalAmount: 0.0 
            })
        });
        if (!response.ok) throw new Error("Erro ao adicionar conta");
        return response.json();
    },

    async updateBill(id: number, updates: Partial<Bill>): Promise<Bill> {
        const response = await fetch(`${ENDPOINT}/bill/${id}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updates)
        });
        if (!response.ok) throw new Error("Erro ao atualizar conta");
        return response.json();
    },

    async deleteBill(id: number): Promise<void> {
        await fetch(`${ENDPOINT}/bill/${id}`, {
            method: 'DELETE',
        });
    }
}