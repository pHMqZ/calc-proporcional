import { describe, it, expect, beforeEach, vi, afterEach } from "vitest";
import { getClientId } from "../utils/session";

describe('User session (Multi-Tenancy)', () => {
    beforeEach(() => {
        localStorage.clear();
        vi.unstubAllEnvs();
        vi.useFakeTimers();
    });

    afterEach(() => {
        vi.restoreAllMocks();
        vi.useRealTimers();
    });

    it('Should generate a new ID and save with TTL in localStorage when not exist', () => {
        vi.stubEnv('DEV', false);

        const mockUUID = '123e4567-e89b-12d3-a456-426614174000';

        vi.stubGlobal('crypto', {
            randomUUID: () => mockUUID
        });

        const id = getClientId();

        expect(id).toBe(mockUUID);
        const stored = JSON.parse(localStorage.getItem('X-Client-Id-Data') || '{}');
        expect(stored.value).toBe(mockUUID);
        expect(stored.expiry).toBeGreaterThan(Date.now());
    });

    it('Should return existent UUID if user reloads the page before TTL expires', () => {
        const savedId = '123e4567-e89b-12d3-a456-426614174000';
        const futureExpiry = Date.now() + 10000;

        localStorage.setItem('X-Client-Id-Data', JSON.stringify({ value: savedId, expiry: futureExpiry }));

        const id = getClientId();

        expect(id).toBe(savedId);
    });

    it('Should generate a new ID if TTL expired', () => {
        vi.stubEnv('DEV', false);
        const expiredId = 'old-expired-uuid';
        const pastExpiry = Date.now() - 10000;
        localStorage.setItem('X-Client-Id-Data', JSON.stringify({ value: expiredId, expiry: pastExpiry }));

        const newMockUUID = 'new-valid-uuid';
        vi.stubGlobal('crypto', {
            randomUUID: () => newMockUUID
        });

        const id = getClientId();

        expect(id).toBe(newMockUUID);
        expect(id).not.toBe(expiredId);
        
        const stored = JSON.parse(localStorage.getItem('X-Client-Id-Data') || '{}');
        expect(stored.value).toBe(newMockUUID);
    });

    it('Should fix seeder mode when env is development', () => {
        vi.stubEnv('DEV', true);

        const id = getClientId();

        expect(id).toBe('seeder-client-dev');
        const stored = JSON.parse(localStorage.getItem('X-Client-Id-Data') || '{}');
        expect(stored.value).toBe('seeder-client-dev');
    });
});