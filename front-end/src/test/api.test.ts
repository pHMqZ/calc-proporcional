import { beforeEach, describe, expect, it, vi } from "vitest";
import { api } from "../services/api";

// Mock do ambiente diretamente no teste
vi.stubGlobal('import', {
    meta: {
        env: {
            VITE_API_URL: 'http://localhost:8080/api/v1'
        }
    }
});

describe('ApiService', () => {
    const fetchMock = vi.fn();

    vi.stubGlobal('fetch', fetchMock);

    beforeEach(() => {
        fetchMock.mockClear();
    });

    it('Should fetch summary data successfully', async () => {
        const mockResponse = {
            totalSalary: 5000,
            totalBills: 2000,
            totalWithReserve: 2500,
            totalRemainder: 2500,
            peopleData: [],
            billsDistribution: []
        };
        fetchMock.mockResolvedValueOnce({
            ok: true,
            json: async () => mockResponse,
        });
        const result = await api.getSummary();

        expect(fetchMock).toHaveBeenCalledWith(expect.stringContaining('/calculation'));
        expect(result.totalSalary).toBe(5000);
        expect(result.totalRemainder).toBe(2500);
    });

    it('Should throw an error if fetch fails', async () => {
        fetchMock.mockResolvedValueOnce({
            ok: false,
        });
        await expect(api.getSummary()).rejects.toThrow("Erro ao buscar resumo de cálculo");
    });
});