import type { Bill } from "../types/Bill";
import type { CalculationSummary } from "../types/Calculation";
import type { Person } from "../types/Person";
import { getClientId } from "../utils/session";

const ENDPOINT = import.meta.env.VITE_API_URL;

const getHeaders = (extraHeaders = {}) => {
    return {
        "Content-Type": "application/json",
        "X-Client-Id": getClientId(),
        ...extraHeaders
    }
}

const handleResponse = async (response: Response) => {
    if (!response.ok) {
        let errorMessage = "Erro de servidor";

        try {
            const errorData = await response.json();
            if (errorData.message) {
                errorMessage = errorData.message;
            }
        } catch (parseError) {

        }

        throw new Error(errorMessage);
    }

    return response.json();
}

export const api = {
    async getSummary(): Promise<CalculationSummary> {
        const response = await fetch(`${ENDPOINT}/calculation`, {
            headers: getHeaders()
        });
        return handleResponse(response);
    },

    async getPeople(): Promise<Person[]> {
        const response = await fetch(`${ENDPOINT}/person`, {
            headers: getHeaders()
        });
        return handleResponse(response);
    },

    async addPerson(name: string, salary: number, reservePercentage: number): Promise<Person> {
        const response = await fetch(`${ENDPOINT}/person`, {
            method: "POST",
            headers: getHeaders(),
            body: JSON.stringify({
                name,
                salary: Number(salary) || 0,
                reservePercentage: Number(reservePercentage) || 0
            })
        });
        return handleResponse(response);
    },

    async updatePerson(id: number, updates: Partial<Person>): Promise<Person> {
        const response = await fetch(`${ENDPOINT}/person/${id}`, {
            method: "PATCH",
            headers: getHeaders(),
            body: JSON.stringify(updates)
        });
        return handleResponse(response);
    },

    async deletePerson(id: number): Promise<void> {
        await fetch(`${ENDPOINT}/person/${id}`, {
            method: 'DELETE',
            headers: getHeaders()
        });
    },

    async getBills(): Promise<Bill[]> {
        const response = await fetch(`${ENDPOINT}/bill`, {
            headers: getHeaders()
        });
        return handleResponse(response);
    },

    async addBill(description: string): Promise<Bill> {
        const response = await fetch(`${ENDPOINT}/bill`, {
            method: "POST",
            headers: getHeaders(),
            body: JSON.stringify({
                description: description || "Nova Conta",
                totalAmount: 0.0
            })
        });
        return handleResponse(response);
    },

    async updateBill(id: number, updates: Partial<Bill>): Promise<Bill> {
        const response = await fetch(`${ENDPOINT}/bill/${id}`, {
            method: "PATCH",
            headers: getHeaders(),
            body: JSON.stringify(updates)
        });
        return handleResponse(response);
    },

    async deleteBill(id: number): Promise<void> {
        await fetch(`${ENDPOINT}/bill/${id}`, {
            method: 'DELETE',
            headers: getHeaders()
        });
    }
}