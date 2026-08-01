const TTL_MS = 24 * 60 * 60 * 1000; // 24 hours

export const getClientId = (): string => {
    let clientId: string | null = null;
    const storedItem = localStorage.getItem('X-Client-Id-Data');

    if (storedItem) {
        try {
            const parsed = JSON.parse(storedItem);
            if (Date.now() < parsed.expiry) {
                clientId = parsed.value;
            } else {
                localStorage.removeItem('X-Client-Id-Data');
            }
        } catch {
            // invalid JSON, ignore and overwrite
        }
    }

    if (!clientId) {
        if (import.meta.env.DEV) {
            clientId = 'seeder-client-dev';
        } else {
            clientId = crypto.randomUUID();
        }
        
        const expiry = Date.now() + TTL_MS;
        localStorage.setItem('X-Client-Id-Data', JSON.stringify({ value: clientId, expiry }));
    }

    return clientId;
}