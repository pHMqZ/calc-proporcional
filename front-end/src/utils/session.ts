
export const getClientId = (): string => {
    let clientId = sessionStorage.getItem('X-Client-Id');

    if (!clientId) {
        if (import.meta.env.DEV) {
            clientId = 'seeder-client-dev';
        } else {
            clientId = crypto.randomUUID();
        }

        sessionStorage.setItem('X-Client-Id', clientId);
    }

    return clientId;
}