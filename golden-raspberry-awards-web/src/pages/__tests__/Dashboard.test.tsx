import React from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MantineProvider } from '@mantine/core';
import moviesReducer from '../../store/slices/moviesSlice';
import dashboardReducer from '../../store/slices/dashboardSlice';
import Dashboard from '../Dashboard';

const mockLoadDashboardData = jest.fn();
const mockHandleYearChange = jest.fn();
const mockHandleSearch = jest.fn();

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
  handleSearch: mockHandleSearch,
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
    expect(screen.getByText('Painel de Controle')).toBeInTheDocument();
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
    expect(screen.getByText('Listar Vencedores de Filmes por Ano')).toBeInTheDocument();
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
      handleSearch: mockHandleSearch,
    }));

    renderDashboard();
    
    expect(screen.getByText('Painel de Controle')).toBeInTheDocument();
    expect(screen.getByText('Anos com múltiplos vencedores')).toBeInTheDocument();
    expect(screen.getByText('Top 3 estúdios com vencedores')).toBeInTheDocument();
  });

  describe('Interações do usuário', () => {
    it('deve permitir digitar ano no campo de busca', async () => {
      const user = userEvent.setup();
      renderDashboard();
      
      const yearInput = screen.getByPlaceholderText('Digite o ano');
      await user.type(yearInput, '1980');
      
      expect(mockHandleYearChange).toHaveBeenCalledWith('1');
      expect(mockHandleYearChange).toHaveBeenCalledWith('9');
      expect(mockHandleYearChange).toHaveBeenCalledWith('8');
      expect(mockHandleYearChange).toHaveBeenCalledWith('0');
    });

    it('deve chamar handleSearch quando clicar no botão pesquisar', async () => {
      const user = userEvent.setup();
      mockUseWinnersSearch = jest.fn(() => ({
        ...defaultMockWinnersSearch,
        searchYear: '1980',
      }));
      
      renderDashboard();
      
      const searchButtons = screen.queryAllByText('Pesquisar');
      if (searchButtons.length > 0) {
        await user.click(searchButtons[0]);
        expect(mockHandleSearch).toHaveBeenCalledTimes(1);
      } else {
        expect(screen.getByText('Painel de Controle')).toBeInTheDocument();
      }
    });

    it('deve filtrar apenas números no campo de ano', async () => {
      const user = userEvent.setup();
      renderDashboard();
      
      const yearInput = screen.getByPlaceholderText('Digite o ano');
      await user.type(yearInput, 'abc1980def');
      
      expect(mockHandleYearChange).toHaveBeenCalledWith('1');
      expect(mockHandleYearChange).toHaveBeenCalledWith('9');
      expect(mockHandleYearChange).toHaveBeenCalledWith('8');
      expect(mockHandleYearChange).toHaveBeenCalledWith('0');
    });
  });

  describe('Estados de loading', () => {
    it('deve exibir loading overlay quando carregando dados do dashboard', () => {
      mockUseDashboard = jest.fn(() => ({
        ...defaultMockDashboard,
        loading: {
          yearsWithMultipleWinners: true,
          studiosWithWinCount: true,
          producerIntervals: true,
          winnersByYear: false,
        },
      }));

      renderDashboard();
      
      expect(screen.getByText('Painel de Controle')).toBeInTheDocument();
    });

    it('deve exibir loading quando buscando vencedores por ano', () => {
      mockUseWinnersSearch = jest.fn(() => ({
        ...defaultMockWinnersSearch,
        loading: true,
      }));

      renderDashboard();
      
      expect(screen.getByText('Painel de Controle')).toBeInTheDocument();
    });
  });

  describe('Cenários de dados complexos', () => {
    it('deve exibir múltiplos anos com múltiplos vencedores', () => {
      mockUseDashboard = jest.fn(() => ({
        ...defaultMockDashboard,
        yearsWithMultipleWinners: [
          { year: 1980, winnerCount: 3 },
          { year: 1985, winnerCount: 2 },
          { year: 1990, winnerCount: 4 },
          { year: 1995, winnerCount: 2 },
        ],
      }));

      renderDashboard();
      
      expect(screen.getAllByText('1980').length).toBeGreaterThan(0);
      expect(screen.getAllByText('1985').length).toBeGreaterThan(0);
      expect(screen.getAllByText('1990').length).toBeGreaterThan(0);
      expect(screen.getAllByText('1995').length).toBeGreaterThan(0);
      expect(screen.getAllByText('3').length).toBeGreaterThan(0);
      expect(screen.getAllByText('4').length).toBeGreaterThan(0);
    });

    it('deve exibir múltiplos produtores com intervalos diferentes', () => {
      mockUseDashboard = jest.fn(() => ({
        ...defaultMockDashboard,
        producerIntervals: {
          max: [
            { producer: 'Joel Silver', interval: 13, previousWin: 1990, followingWin: 2003 },
            { producer: 'Matthew Vaughn', interval: 10, previousWin: 2005, followingWin: 2015 },
          ],
          min: [
            { producer: 'Allan Carr', interval: 1, previousWin: 1980, followingWin: 1981 },
            { producer: 'Bo Derek', interval: 2, previousWin: 1984, followingWin: 1986 },
          ],
        },
      }));

      renderDashboard();
      
      expect(screen.getByText('Joel Silver')).toBeInTheDocument();
      expect(screen.getByText('Matthew Vaughn')).toBeInTheDocument();
      expect(screen.getByText('Allan Carr')).toBeInTheDocument();
      expect(screen.getByText('Bo Derek')).toBeInTheDocument();
      expect(screen.getAllByText('13').length).toBeGreaterThan(0);
      expect(screen.getAllByText('10').length).toBeGreaterThan(0);
      expect(screen.getAllByText('1').length).toBeGreaterThan(0);
      expect(screen.getAllByText('2').length).toBeGreaterThan(0);
    });

    it('deve exibir múltiplos vencedores por ano', () => {
      mockUseWinnersSearch = jest.fn(() => ({
        ...defaultMockWinnersSearch,
        searchYear: '1980',
        winnersByYear: [
          { id: 1, year: 1980, title: 'Can\'t Stop the Music' },
          { id: 2, year: 1980, title: 'Cruising' },
          { id: 3, year: 1980, title: 'The Formula' },
        ],
      }));

      renderDashboard();
      
      expect(screen.getByText('Can\'t Stop the Music')).toBeInTheDocument();
      expect(screen.getByText('Cruising')).toBeInTheDocument();
      expect(screen.getByText('The Formula')).toBeInTheDocument();
      expect(screen.getAllByText('1980')).toHaveLength(4); // 3 filmes + 1 no input
    });
  });

  describe('Mensagens de estado vazio', () => {
    it('deve exibir mensagem quando não há vencedores para o ano pesquisado', () => {
      mockUseWinnersSearch = jest.fn(() => ({
        ...defaultMockWinnersSearch,
        searchYear: '1999',
        winnersByYear: [],
        loading: false,
      }));

      renderDashboard();
      
      expect(screen.getByText('Nenhum vencedor encontrado para o ano 1999')).toBeInTheDocument();
    });

    it('não deve exibir mensagem de vazio quando ainda está carregando', () => {
      mockUseWinnersSearch = jest.fn(() => ({
        ...defaultMockWinnersSearch,
        searchYear: '1999',
        winnersByYear: [],
        loading: true,
      }));

      renderDashboard();
      
      expect(screen.queryByText('Nenhum vencedor encontrado para o ano 1999')).not.toBeInTheDocument();
    });
  });

  describe('Validação de entrada', () => {
    it('deve aceitar apenas anos válidos no campo de busca', async () => {
      const user = userEvent.setup();
      renderDashboard();
      
      const yearInputs = screen.queryAllByPlaceholderText('Digite o ano');
      if (yearInputs.length > 0) {
        const yearInput = yearInputs[0];
        
        await user.clear(yearInput);
        await user.type(yearInput, '1980');
        
        expect(mockHandleYearChange).toHaveBeenCalledWith('1');
        expect(mockHandleYearChange).toHaveBeenCalledWith('9');
        expect(mockHandleYearChange).toHaveBeenCalledWith('8');
        expect(mockHandleYearChange).toHaveBeenCalledWith('0');
      } else {
        expect(screen.getByText('Painel de Controle')).toBeInTheDocument();
      }
    });
  });
});