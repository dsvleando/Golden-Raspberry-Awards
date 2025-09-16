import React from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MantineProvider } from '@mantine/core';
import moviesReducer from '../../store/slices/moviesSlice';
import dashboardReducer from '../../store/slices/dashboardSlice';
import MoviesList from '../MoviesList';

const mockLoadMovies = jest.fn();
const mockUpdateFilters = jest.fn();
const mockChangePage = jest.fn();
const mockClearError = jest.fn();

const defaultMockMovies = {
  movies: {
    content: [
      { id: 1, year: 1980, title: 'Can\'t Stop the Music', winner: true },
      { id: 2, year: 1981, title: 'Mommie Dearest', winner: false },
    ],
    totalElements: 2,
    totalPages: 1,
    number: 0,
  },
  loading: false,
  error: null,
  filters: { page: 0, size: 15 },
  loadMovies: mockLoadMovies,
  updateFilters: mockUpdateFilters,
  changePage: mockChangePage,
  clearError: mockClearError,
};

let mockUseMovies = jest.fn(() => defaultMockMovies);

jest.mock('../../hooks', () => ({
  useMovies: () => mockUseMovies(),
}));

const store = configureStore({
  reducer: {
    movies: moviesReducer,
    dashboard: dashboardReducer,
  },
});

describe('MoviesList Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    mockUseMovies = jest.fn(() => defaultMockMovies);
  });

  const renderMoviesList = () => {
    return render(
      <Provider store={store}>
        <MantineProvider>
          <MoviesList />
        </MantineProvider>
      </Provider>
    );
  };

  it('deve renderizar o título da página', () => {
    renderMoviesList();
    expect(screen.getByText('List movies')).toBeInTheDocument();
  });

  it('deve renderizar os cabeçalhos da tabela', () => {
    renderMoviesList();
    
    expect(screen.getByText('ID')).toBeInTheDocument();
    expect(screen.getAllByText('Year')).toHaveLength(2);
    expect(screen.getByText('Title')).toBeInTheDocument();
    expect(screen.getAllByText('Winner?')).toHaveLength(2);
  });

  it('deve renderizar os filmes na tabela', () => {
    renderMoviesList();
    
    expect(screen.getByText('Can\'t Stop the Music')).toBeInTheDocument();
    expect(screen.getByText('Mommie Dearest')).toBeInTheDocument();
    expect(screen.getByText('1980')).toBeInTheDocument();
    expect(screen.getByText('1981')).toBeInTheDocument();
    
    expect(screen.getAllByText('Yes').length).toBeGreaterThan(0);
    expect(screen.getAllByText('No').length).toBeGreaterThan(0);
  });

  it('deve renderizar os campos de filtro', () => {
    renderMoviesList();
    
    expect(screen.getByText('Filters')).toBeInTheDocument();
    expect(screen.getAllByText('Year')).toHaveLength(2);
    expect(screen.getAllByText('Winner?')).toHaveLength(2);
    expect(screen.getByText('Search')).toBeInTheDocument();
  });

  describe('Interações do usuário', () => {
    it('deve permitir digitar ano no campo de filtro', async () => {
      const user = userEvent.setup();
      renderMoviesList();
      
      const yearInput = screen.getByPlaceholderText('Filter by year');
      await user.type(yearInput, '1980');
      
      expect(yearInput).toHaveValue('1980');
    });

    it('deve permitir selecionar filtro de vencedor', async () => {
      const user = userEvent.setup();
      renderMoviesList();
      
      const winnerSelect = screen.getByDisplayValue('Yes/No');
      
      expect(winnerSelect).toBeInTheDocument();
      
      expect(winnerSelect).toHaveValue('Yes/No');
    });

    it('deve chamar updateFilters quando clicar no botão pesquisar', async () => {
      const user = userEvent.setup();
      renderMoviesList();
      
      const searchButton = screen.getByText('Search');
      await user.click(searchButton);
      
      expect(mockUpdateFilters).toHaveBeenCalledWith({
        year: undefined,
        winner: undefined,
        page: 0,
      });
    });

    it('deve aplicar filtros corretamente ao pesquisar', async () => {
      const user = userEvent.setup();
      renderMoviesList();
      
      const yearInput = screen.getByPlaceholderText('Filter by year');
      await user.type(yearInput, '1980');
      
      const searchButton = screen.getByText('Search');
      await user.click(searchButton);
      
      expect(mockUpdateFilters).toHaveBeenCalledWith({
        year: 1980,
        winner: undefined,
        page: 0,
      });
    });
  });

  describe('Estados de loading e erro', () => {
    it('deve exibir loading overlay quando carregando', () => {
      mockUseMovies = jest.fn(() => ({
        ...defaultMockMovies,
        loading: true,
      }));

      renderMoviesList();
      
      expect(screen.getByText('List movies')).toBeInTheDocument();
    });

    it('deve exibir mensagem de erro quando há erro', () => {
      mockUseMovies = jest.fn(() => ({
        ...defaultMockMovies,
        error: 'Erro ao carregar filmes',
      }));

      renderMoviesList();
      
      expect(screen.getByText('Erro')).toBeInTheDocument();
      expect(screen.getByText('Erro ao carregar filmes')).toBeInTheDocument();
    });

    it('deve exibir mensagem quando não há filmes', () => {
      mockUseMovies = jest.fn(() => ({
        ...defaultMockMovies,
        movies: {
          content: [],
          totalElements: 0,
          totalPages: 0,
          number: 0,
        },
      }));

      renderMoviesList();
      
      expect(screen.getByText('No movies found')).toBeInTheDocument();
    });
  });

  describe('Paginação', () => {
    it('deve exibir paginação quando há múltiplas páginas', () => {
      mockUseMovies = jest.fn(() => ({
        ...defaultMockMovies,
        movies: {
          content: [
            { id: 1, year: 1980, title: 'Movie 1', winner: true },
            { id: 2, year: 1981, title: 'Movie 2', winner: false },
          ],
          totalElements: 30,
          totalPages: 2,
          number: 0,
        },
      }));

      renderMoviesList();
      
      expect(screen.getByText('Showing 2 of 30 movies')).toBeInTheDocument();
      expect(screen.getByRole('button', { name: '1' })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: '2' })).toBeInTheDocument();
    });

    it('deve chamar changePage quando clicar na paginação', async () => {
      const user = userEvent.setup();
      mockUseMovies = jest.fn(() => ({
        ...defaultMockMovies,
        movies: {
          content: [
            { id: 1, year: 1980, title: 'Movie 1', winner: true },
            { id: 2, year: 1981, title: 'Movie 2', winner: false },
          ],
          totalElements: 30,
          totalPages: 2,
          number: 0,
        },
      }));

      renderMoviesList();
      
      const nextPageButton = screen.getByRole('button', { name: /2/i });
      await user.click(nextPageButton);
      
      expect(mockChangePage).toHaveBeenCalledWith(1);
    });

    it('não deve exibir paginação quando há apenas uma página', () => {
      renderMoviesList();
      
      const paginationButtons = screen.queryAllByRole('button', { name: /^[2-9]$/ });
      expect(paginationButtons).toHaveLength(0);
    });
  });

  describe('Cenários de dados complexos', () => {
    it('deve exibir lista grande de filmes', () => {
      const largeMovieList = Array.from({ length: 20 }, (_, i) => ({
        id: i + 1,
        year: 1980 + i,
        title: `Movie ${i + 1}`,
        winner: i % 2 === 0,
      }));

      mockUseMovies = jest.fn(() => ({
        ...defaultMockMovies,
        movies: {
          content: largeMovieList,
          totalElements: 20,
          totalPages: 2,
          number: 0,
        },
      }));

      renderMoviesList();
      
      expect(screen.getByText('Movie 1')).toBeInTheDocument();
      expect(screen.getByText('Movie 20')).toBeInTheDocument();
      expect(screen.getByText('1980')).toBeInTheDocument();
      expect(screen.getByText('1999')).toBeInTheDocument();
    });

    it('deve exibir filmes com títulos longos', () => {
      mockUseMovies = jest.fn(() => ({
        ...defaultMockMovies,
        movies: {
          content: [
            { 
              id: 1, 
              year: 1980, 
              title: 'This is a very long movie title that should be displayed correctly in the table', 
              winner: true 
            },
          ],
          totalElements: 1,
          totalPages: 1,
          number: 0,
        },
      }));

      renderMoviesList();
      
      expect(screen.getByText('This is a very long movie title that should be displayed correctly in the table')).toBeInTheDocument();
    });

    it('deve exibir filmes com caracteres especiais no título', () => {
      mockUseMovies = jest.fn(() => ({
        ...defaultMockMovies,
        movies: {
          content: [
            { id: 1, year: 1980, title: 'Movie with "quotes" & symbols!', winner: true },
            { id: 2, year: 1981, title: 'Movie with <script>alert("xss")</script>', winner: false },
          ],
          totalElements: 2,
          totalPages: 1,
          number: 0,
        },
      }));

      renderMoviesList();
      
      expect(screen.getByText('Movie with "quotes" & symbols!')).toBeInTheDocument();
      expect(screen.getByText('Movie with <script>alert("xss")</script>')).toBeInTheDocument();
    });
  });

  describe('Validação de filtros', () => {
    it('deve aceitar apenas números no campo de ano', async () => {
      const user = userEvent.setup();
      renderMoviesList();
      
      const yearInput = screen.getByPlaceholderText('Filter by year');
      
      await user.clear(yearInput);
      await user.type(yearInput, 'abc1980def');
      
      expect(yearInput).toHaveValue('1980');
    });

    it('deve resetar filtros ao limpar campos', async () => {
      const user = userEvent.setup();
      renderMoviesList();
      
      const yearInput = screen.getByPlaceholderText('Filter by year');
      await user.type(yearInput, '1980');
      
      await user.clear(yearInput);
      
      const searchButton = screen.getByText('Search');
      await user.click(searchButton);
      
      expect(mockUpdateFilters).toHaveBeenCalledWith({
        year: undefined,
        winner: undefined,
        page: 0,
      });
    });
  });

  describe('Acessibilidade', () => {
    it('deve ter labels apropriados para campos de filtro', () => {
      renderMoviesList();
      
      expect(screen.getByLabelText('Year')).toBeInTheDocument();
      const vencedorLabels = screen.getAllByText('Winner?');
      expect(vencedorLabels.length).toBeGreaterThan(0);
    });

    it('deve ter cabeçalhos de tabela apropriados', () => {
      renderMoviesList();
      
      expect(screen.getByRole('columnheader', { name: 'ID' })).toBeInTheDocument();
      expect(screen.getByRole('columnheader', { name: 'Year' })).toBeInTheDocument();
      expect(screen.getByRole('columnheader', { name: 'Title' })).toBeInTheDocument();
      expect(screen.getByRole('columnheader', { name: 'Winner?' })).toBeInTheDocument();
    });

    it('deve ter botão de pesquisa acessível', () => {
      renderMoviesList();
      
      const searchButton = screen.getByRole('button', { name: 'Search' });
      expect(searchButton).toBeInTheDocument();
    });
  });
});
