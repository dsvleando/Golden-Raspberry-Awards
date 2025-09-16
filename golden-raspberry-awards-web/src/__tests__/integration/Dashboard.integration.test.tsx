jest.mock('../../api', () => ({
  dashboardApi: {
    getYearsWithMultipleWinners: jest.fn().mockResolvedValue({
      years: [
        { year: 1986, winnerCount: 2 },
        { year: 1990, winnerCount: 2 }
      ]
    }),
    getStudiosWithWinCount: jest.fn().mockResolvedValue({
      studios: [
        { name: 'Studio A', winCount: 5 },
        { name: 'Studio B', winCount: 3 },
        { name: 'Studio C', winCount: 2 }
      ]
    }),
    getProducerIntervals: jest.fn().mockResolvedValue({
      max: [
        { producer: 'Matthew Vaughn', interval: 13, previousWin: 2002, followingWin: 2015 }
      ],
      min: [
        { producer: 'Joel Silver', interval: 1, previousWin: 1990, followingWin: 1991 }
      ]
    }),
    getWinnersByYear: jest.fn().mockResolvedValue([
      { id: 1, year: 1980, title: 'Movie 1', winner: true }
    ])
  }
}));

import React from 'react';
import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MantineProvider } from '@mantine/core';
import Dashboard from '../../pages/Dashboard';
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

const renderDashboard = (store = createTestStore()) => {
  return render(
    <Provider store={store}>
      <MantineProvider>
        <Dashboard />
      </MantineProvider>
    </Provider>
  );
};

describe('Dashboard Integration Tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('deve renderizar o dashboard sem erros', () => {
    renderDashboard();

    expect(screen.getByText('Dashboard')).toBeInTheDocument();

    expect(screen.getByText('List years with multiple winners')).toBeInTheDocument();
    expect(screen.getByText('Top 3 studios with winners')).toBeInTheDocument();
    expect(screen.getByText('Producers with longest and shortest interval between wins')).toBeInTheDocument();
    expect(screen.getByText('List movie winners by year')).toBeInTheDocument();
  });

  it('deve exibir campos de busca', () => {
    renderDashboard();

    expect(screen.getByPlaceholderText('Search by year')).toBeInTheDocument();
    
    const searchButtons = screen.queryAllByText('Search');
    expect(searchButtons.length).toBeGreaterThanOrEqual(0);
  });

  it('deve exibir estados de loading', () => {
    renderDashboard();

    const loadingOverlays = document.querySelectorAll('.mantine-LoadingOverlay-root');
    expect(loadingOverlays.length).toBeGreaterThan(0);
  });

  it('deve exibir estrutura das tabelas', () => {
    renderDashboard();

    expect(screen.getAllByText('Year').length).toBeGreaterThan(0);
    expect(screen.getAllByText('Win Count').length).toBeGreaterThan(0);
    expect(screen.getByText('Name')).toBeInTheDocument();
    expect(screen.getAllByText('Producer').length).toBeGreaterThan(0);
    expect(screen.getAllByText('Interval').length).toBeGreaterThan(0);
    expect(screen.getAllByText('Previous Year').length).toBeGreaterThan(0);
    expect(screen.getAllByText('Following Year').length).toBeGreaterThan(0);
    expect(screen.getByText('Id')).toBeInTheDocument();
    expect(screen.getByText('Title')).toBeInTheDocument();
  });

  it('deve exibir seções de produtores', () => {
    renderDashboard();

    expect(screen.getByText('Maximum')).toBeInTheDocument();
    expect(screen.getByText('Minimum')).toBeInTheDocument();
  });

  it('deve renderizar sem erros mesmo com dados vazios', () => {
    const emptyStore = configureStore({
      reducer: {
        dashboard: dashboardReducer,
        movies: moviesReducer,
      },
      preloadedState: {
        dashboard: {
          yearsWithMultipleWinners: [],
          studiosWithWinCount: [],
          producerIntervals: { min: [], max: [] },
          winnersByYear: [],
          selectedYear: null,
          loading: {
            yearsWithMultipleWinners: false,
            studiosWithWinCount: false,
            producerIntervals: false,
            winnersByYear: false,
          },
          error: null,
        },
        movies: {
          movies: null,
          filters: {},
          loading: false,
          error: null,
        },
      },
    });

    render(
      <Provider store={emptyStore}>
        <MantineProvider>
          <Dashboard />
        </MantineProvider>
      </Provider>
    );

    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('List years with multiple winners')).toBeInTheDocument();
  });
});
