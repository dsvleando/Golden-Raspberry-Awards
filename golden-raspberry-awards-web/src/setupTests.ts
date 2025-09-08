import '@testing-library/jest-dom';

// Mock do ResizeObserver para o Mantine UI
class MockResizeObserver {
  observe = jest.fn();
  unobserve = jest.fn();
  disconnect = jest.fn();
}

global.ResizeObserver = MockResizeObserver;
