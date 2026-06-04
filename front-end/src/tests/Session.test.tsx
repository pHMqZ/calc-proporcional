import { describe, it, expect, beforeEach, vi, afterEach } from "vitest";
import { getClientId } from "../utils/session";

describe('User session (Multi-Tenancy)', () => {
    beforeEach(() => {
        sessionStorage.clear();
        vi.unstubAllEnvs();
    });

    afterEach(() => {
        vi.restoreAllMocks();
    });

    it('Should generate a new ID when not exist in session', () => {

        vi.stubEnv('DEV', false);

        const mockUUID = '123e4567-e89b-12d3-a456-426614174000';

        vi.stubGlobal('crypto', {
            randomUUID: () => mockUUID
        });

        const id = getClientId();

        expect(id).toBe(mockUUID);
        expect(sessionStorage.getItem('X-Client-Id')).toBe(mockUUID);

    });

    it('Should return existent UUID if user reload the page', () => {

        const savedIdInSession = '123e4567-e89b-12d3-a456-426614174000';

        sessionStorage.setItem('X-Client-Id', savedIdInSession);

        const id = getClientId();

        expect(id).toBe(savedIdInSession);

    });

    it('Should fix seeder mode when env is development', () => {
        vi.stubEnv('DEV', true);

        const id = getClientId();

        expect(id).toBe('seeder-client-dev');
        expect(sessionStorage.getItem('X-Client-Id')).toBe('seeder-client-dev');

    });
});