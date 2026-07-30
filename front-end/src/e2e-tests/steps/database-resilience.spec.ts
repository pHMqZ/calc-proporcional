import { test, expect } from '@playwright/test';
import { execSync } from 'node:child_process';
import { randomUUID } from 'node:crypto';

test.describe('PostgreSQL Database Resilience', () => {

  test('Should persist data after database container restart', async ({ request }) => {
    // We use a unique clientId for this test to isolate data
    const clientId = randomUUID();

    // 1. Insert Person via API
    const personPayload = {
      name: 'Carlos',
      salary: 5000,
      reservePercentage: 0,
      clientId: clientId
    };

    const personResponse = await request.post('http://localhost:8080/api/v1/person', {
      headers: { 'X-Client-Id': clientId },
      data: personPayload
    });
    expect(personResponse.status()).toBe(201);

    // 2. Insert Bill via API
    const billPayload = {
      description: 'Aluguel',
      totalAmount: 2000,
      clientId: clientId
    };

    const billResponse = await request.post('http://localhost:8080/api/v1/bill', {
      headers: { 'X-Client-Id': clientId },
      data: billPayload
    });
    expect(billResponse.status()).toBe(201);

    // 3. Restart Postgres Docker Container
    console.log('Restarting postgres container...');
    // Utilizing raw docker command to bypass path resolution (sanitized approach)
    execSync('docker restart calcprop-postgres', { stdio: 'inherit' });

    // 4. Validate Data Persistence (using expect.poll instead of static timeout)
    await expect.poll(
      async () => {
        try {
          const res = await request.get('http://localhost:8080/api/v1/person', {
            headers: { 'X-Client-Id': clientId }
          });
          if (res.status() === 200) {
            return res.status();
          }
          return 0;
        } catch (e) {
          return 0; // Backend may be reconnecting to DB
        }
      },
      {
        message: 'Backend should reconnect and return 200',
        timeout: 15000,
        intervals: [1000, 2000, 3000]
      }
    ).toBe(200);

    const getPersonResponse = await request.get('http://localhost:8080/api/v1/person', {
      headers: { 'X-Client-Id': clientId }
    });
    const peopleData = await getPersonResponse.json();

    // Assert person exists
    const person = peopleData.find((p: any) => p.name === 'Carlos');
    expect(person).toBeDefined();
    expect(person.salary).toBe(5000);

    const getBillResponse = await request.get('http://localhost:8080/api/v1/bill', {
      headers: { 'X-Client-Id': clientId }
    });
    const billData = await getBillResponse.json();

    // Assert bill exists
    const bill = billData.find((b: any) => b.description === 'Aluguel');
    expect(bill).toBeDefined();
    expect(bill.totalAmount).toBe(2000);
  });

});
