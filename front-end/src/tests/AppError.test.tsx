/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import App from '../App';
import * as api from '../services/api';

vi.mock('../services/api', () => ({
    api: {
        getSummary: vi.fn(),
        getPeople: vi.fn(),
        getBills: vi.fn(),
    }
}));

describe('Global error flow', () => {
    it('Should display error banner when API fails', async () => {
        const errorMessage = 'API Error';

        (api.api.getSummary as any).mockRejectedValue(new Error(errorMessage));
        (api.api.getPeople as any).mockResolvedValue([]);
        (api.api.getBills as any).mockResolvedValue([]);

        render(<App />);

        await waitFor(() => {
            expect(screen.getByText(/Atenção/i)).toBeInTheDocument();
            expect(screen.getByText(new RegExp(errorMessage, 'i'))).toBeInTheDocument();
        }, { timeout: 3000 });
    });
});
