import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { api } from '../services/api';

describe('Api service(Fetch)', () => {
    beforeEach(() => {
        localStorage.clear();

        vi.stubGlobal('fetch', vi.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve([])
            })));
    });

    afterEach(() => {
        vi.restoreAllMocks();
    });

    it('Should inject header X-Client-Id in request fetch', async () => {

        const myTestId = '123e4567-e89b-12d3-a456-426614174000';
        const futureExpiry = Date.now() + 10000;
        localStorage.setItem('X-Client-Id-Data', JSON.stringify({ value: myTestId, expiry: futureExpiry }));

        await api.getPeople();

        const mockedFetch = vi.mocked(fetch);

        expect(mockedFetch).toHaveBeenCalledTimes(1);

        const calls = mockedFetch.mock.calls[0];
        const requestInitParams = calls[1];

        expect(requestInitParams?.headers).toHaveProperty('X-Client-Id', myTestId);
    })
})