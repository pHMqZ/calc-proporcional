import { expect, afterEach } from 'vitest';
import { cleanup } from '@testing-library/react';
import * as matchers from '@testing-library/jest-dom/matchers';

// Estende o expect do Vitest com os matchers do Jest-DOM (toBeInTheDocument, etc)
expect.extend(matchers);

// Limpa o DOM após cada teste para evitar poluição entre eles
afterEach(() => {
  cleanup();
});
