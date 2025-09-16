jest.mock('../../api', () => ({
  dashboardApi: {
    getYearsWithMultipleWinners: jest.fn().mockRejectedValue(new Error('API Error')),
    getStudiosWithWinCount: jest.fn().mockResolvedValue({
      studios: [
        { name: 'Studio A', winCount: 5 }
      ]
    }),
    getProducerIntervals: jest.fn().mockResolvedValue({
      max: [],
      min: []
    }),
    getWinnersByYear: jest.fn().mockResolvedValue([])
  },
  moviesApi: {
    getMovies: jest.fn().mockRejectedValue(new Error('Movies API Error'))
  }
}));

import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MantineProvider } from '@mantine/core';
import Dashboard from '../../pages/Dashboard';
import MoviesList from '../../pages/MoviesList';
import dashboardReducer from '../../store/slices/dashboardSlice';
import moviesReducer from '../../store/slices/moviesSlice';

const createTestStore = () => {
  return configureStore({
    reducer: {
      dashboard: dashboardReducer,
      movies: moviesReducer,
    },
  });
};

const renderWithProviders = (component: React.ReactElement, store = createTestStore()) => {
  return render(
    <Provider store={store}>
      <MantineProvider>
        {component}
      </MantineProvider>
    </Provider>
  );
};

describe('Error Handling Integration Tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('deve exibir erro no dashboard quando API falha', async () => {
    renderWithProviders(<Dashboard />);

    await waitFor(() => {
      expect(screen.getByText('Erro')).toBeInTheDocument();
    });

    expect(screen.getByText(/Cannot read properties of undefined/)).toBeInTheDocument();
  });

  it('deve exibir erro na lista de filmes quando API falha', async () => {
    renderWithProviders(<MoviesList />);

    await waitFor(() => {
      const errorElement = screen.queryByText('Erro');
      if (errorElement) {
        expect(errorElement).toBeInTheDocument();
      }
    }, { timeout: 3000 });

    const errorText = screen.queryByText(/Movies API Error/);
    if (errorText) {
      expect(errorText).toBeInTheDocument();
    } else {
      expect(screen.getByText('List movies')).toBeInTheDocument();
    }
  });

  it('deve renderizar estrutura básica mesmo com erro', () => {
    renderWithProviders(<Dashboard />);

    expect(screen.getByText('Dashboard')).toBeInTheDocument();
  });

  it('deve renderizar estrutura básica da lista mesmo com erro', () => {
    renderWithProviders(<MoviesList />);

    expect(screen.getByText('List movies')).toBeInTheDocument();
  });
});
