import { APIRequestContext, APIResponse } from '@playwright/test';

export class ApiHelper {
  private request: APIRequestContext;

  constructor(request: APIRequestContext) {
    this.request = request;
  }

  /**
   * Sends a GET request to the given endpoint with the specified X-Client-Id header.
   */
  async getWithClientHeader(endpoint: string, clientId?: string): Promise<APIResponse> {
    const headers: Record<string, string> = {};
    if (clientId !== undefined) {
      headers['X-Client-Id'] = clientId;
    }

    return this.request.get(endpoint, {
      headers,
    });
  }
}
