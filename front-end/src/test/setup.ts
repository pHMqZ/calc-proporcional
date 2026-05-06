import '@testing-library/jest-dom';
import { vi } from 'vitest';

// Mock do import.meta.env para os testes
vi.stubGlobal('import', {
  meta: {
    env: {
      VITE_API_URL: 'http://localhost:8080/api/v1'
    }
  }
});
