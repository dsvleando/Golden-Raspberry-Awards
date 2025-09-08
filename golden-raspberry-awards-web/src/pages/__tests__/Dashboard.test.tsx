import React from 'react';
import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MantineProvider } from '@mantine/core';
import moviesReducer from '../../store/slices/moviesSlice';
import dashboardReducer from '../../store/slices/dashboardSlice';
import Dashboard from '../Dashboard';

const mockLoadDashboardData = jest.fn();
const mockHandleYearChange = jest.fn();
const mockHandleYearBlur = jest.fn();

const defaultMockDashboard = {
  yearsWithMultipleWinners: [
    { year: 1986, winnerCount: 2 },
    { year: 1990, winnerCount: 2 },
  ],
  studiosWithWinCount: [
    { name: 'Columbia Pictures', winCount: 7 },
    { name: 'Paramount Pictures', winCount: 6 },
    { name: 'Warner Bros.', winCount: 5 },
    { name: 'Universal Pictures', winCount: 4 },
  ],
  producerIntervals: {
    max: [{ producer: 'Joel Silver', interval: 13, previousWin: 1990, followingWin: 2003 }],
    min: [{ producer: 'Allan Carr', interval: 1, previousWin: 1980, followingWin: 1981 }],
  },
  loading: {
    yearsWithMultipleWinners: false,
    studiosWithWinCount: false,
    producerIntervals: false,
    winnersByYear: false,
  },
  error: null,
  loadDashboardData: mockLoadDashboardData,
};

const defaultMockWinnersSearch = {
  searchYear: '',
  winnersByYear: [
    { id: 1, year: 1980, title: 'Can\'t Stop the Music' },
    { id: 2, year: 1980, title: 'Cruising' },
  ],
  loading: false,
  error: null,
  handleYearChange: mockHandleYearChange,
  handleYearBlur: mockHandleYearBlur,
};

let mockUseDashboard = jest.fn(() => defaultMockDashboard);
let mockUseWinnersSearch = jest.fn(() => defaultMockWinnersSearch);

jest.mock('../../hooks', () => ({
  useDashboard: () => mockUseDashboard(),
  useWinnersSearch: () => mockUseWinnersSearch(),
}));

const store = configureStore({
  reducer: {
    movies: moviesReducer,
    dashboard: dashboardReducer,
  },
});

describe('Dashboard Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    mockUseDashboard = jest.fn(() => defaultMockDashboard);
    mockUseWinnersSearch = jest.fn(() => defaultMockWinnersSearch);
  });

  const renderDashboard = () => {
    return render(
      <Provider store={store}>
        <MantineProvider>
          <Dashboard />
        </MantineProvider>
      </Provider>
    );
  };

  it('deve renderizar o título do dashboard', () => {
    renderDashboard();
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
  });

  it('deve chamar loadDashboardData no useEffect', () => {
    renderDashboard();
    expect(mockLoadDashboardData).toHaveBeenCalledTimes(1);
  });

  it('deve renderizar todos os painéis quando há dados', () => {
    renderDashboard();
    
    expect(screen.getByText('Anos com múltiplos vencedores')).toBeInTheDocument();
    expect(screen.getByText('Top 3 estúdios com vencedores')).toBeInTheDocument();
    expect(screen.getByText('Produtores com maior e menor intervalo entre vitórias')).toBeInTheDocument();
    expect(screen.getByText('Listar vencedores de filmes por ano')).toBeInTheDocument();
  });

  it('deve exibir dados dos anos com múltiplos vencedores', () => {
    renderDashboard();
    
    expect(screen.getByText('1986')).toBeInTheDocument();
    expect(screen.getAllByText('1990').length).toBeGreaterThan(0); // Pode aparecer múltiplas vezes
  });

  it('deve exibir apenas os top 3 estúdios', () => {
    renderDashboard();
    
    expect(screen.getByText('Columbia Pictures')).toBeInTheDocument();
    expect(screen.getByText('Paramount Pictures')).toBeInTheDocument();
    expect(screen.getByText('Warner Bros.')).toBeInTheDocument();
    
    expect(screen.queryByText('Universal Pictures')).not.toBeInTheDocument();
  });

  it('deve exibir dados dos intervalos de produtores', () => {
    renderDashboard();
    
    expect(screen.getByText('Joel Silver')).toBeInTheDocument();
    expect(screen.getByText('Allan Carr')).toBeInTheDocument();
  });

  it('deve exibir mensagem de erro quando há erro no dashboard', () => {
    mockUseDashboard = jest.fn(() => ({
      ...defaultMockDashboard,
      error: 'Erro ao carregar dados do dashboard',
    }));

    renderDashboard();
    
    expect(screen.getByText('Erro')).toBeInTheDocument();
    expect(screen.getByText('Erro ao carregar dados do dashboard')).toBeInTheDocument();
    expect(screen.queryByText('Dashboard')).not.toBeInTheDocument();
  });

  it('deve exibir mensagem de erro quando há erro na busca de vencedores', () => {
    mockUseWinnersSearch = jest.fn(() => ({
      ...defaultMockWinnersSearch,
      error: 'Erro ao buscar vencedores',
    }));

    renderDashboard();
    
    expect(screen.getByText('Erro')).toBeInTheDocument();
    expect(screen.getByText('Erro ao buscar vencedores')).toBeInTheDocument();
    expect(screen.queryByText('Dashboard')).not.toBeInTheDocument();
  });

  it('deve priorizar erro do dashboard sobre erro de vencedores', () => {
    mockUseDashboard = jest.fn(() => ({
      ...defaultMockDashboard,
      error: 'Erro do dashboard',
    }));
    mockUseWinnersSearch = jest.fn(() => ({
      ...defaultMockWinnersSearch,
      error: 'Erro dos vencedores',
    }));

    renderDashboard();
    
    expect(screen.getByText('Erro do dashboard')).toBeInTheDocument();
    expect(screen.queryByText('Erro dos vencedores')).not.toBeInTheDocument();
  });

  it('deve renderizar com dados vazios sem quebrar', () => {
    mockUseDashboard = jest.fn(() => ({
      yearsWithMultipleWinners: [],
      studiosWithWinCount: [],
      producerIntervals: { max: [], min: [] },
      loading: {
        yearsWithMultipleWinners: false,
        studiosWithWinCount: false,
        producerIntervals: false,
        winnersByYear: false,
      },
      error: null,
      loadDashboardData: mockLoadDashboardData,
    }));
    mockUseWinnersSearch = jest.fn(() => ({
      searchYear: '',
      winnersByYear: [],
      loading: false,
      error: null,
      handleYearChange: mockHandleYearChange,
      handleYearBlur: mockHandleYearBlur,
    }));

    renderDashboard();
    
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Anos com múltiplos vencedores')).toBeInTheDocument();
    expect(screen.getByText('Top 3 estúdios com vencedores')).toBeInTheDocument();
  });
});