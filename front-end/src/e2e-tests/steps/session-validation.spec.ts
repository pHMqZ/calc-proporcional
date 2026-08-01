import { test, expect } from '@playwright/test';
import { ApiHelper } from '../pages/ApiHelper';
import testData from '../data/session-validation-data.json' assert { type: 'json' };

test.describe('Strict Session Validation per Client (Multi-Tenancy)', () => {

  // T005: US3 - Missing Header
  test('should block request with missing X-Client-Id header and return 400 Bad Request', async ({ request }) => {
    const apiHelper = new ApiHelper(request);
    const response = await apiHelper.getWithClientHeader(testData.apiEndpoint); // Undefined clientId

    expect(response.status()).toBe(400);
    const body = await response.json();
    expect(body.message).toBe(testData.expectedErrorMessage);
  });

  // T005: US3 - Malformed Header
  test('should block request with malformed X-Client-Id header and return 502 Bad Gateway', async ({ request }) => {
    const apiHelper = new ApiHelper(request);
    const response = await apiHelper.getWithClientHeader(testData.apiEndpoint, testData.malformedUuid);

    expect(response.status()).toBe(502);
    const body = await response.json();
    expect(body.message).toBe(testData.expectedErrorMessage);
  });

  // T008: US1 - Valid UUID
  test('should allow request with valid UUID format', async ({ request }) => {
    const apiHelper = new ApiHelper(request);
    const response = await apiHelper.getWithClientHeader(testData.apiEndpoint, testData.validUuid);

    // Note: The endpoint might return 404 or 200 depending on if it exists,
    // but it should NOT return 400 Bad Request from the interceptor.
    expect(response.status()).not.toBe(400);
  });

  // T010: US2 - Dev Client Bypass
  test('should allow request with seeder-client-dev when in dev profile', async ({ request }) => {
    const apiHelper = new ApiHelper(request);
    const response = await apiHelper.getWithClientHeader(testData.apiEndpoint, testData.devSeederId);

    // Assuming the backend is running in 'dev' profile as per test prerequisites.
    expect(response.status()).not.toBe(400);
  });

});
