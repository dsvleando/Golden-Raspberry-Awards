import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MantineProvider } from '@mantine/core';
import moviesReducer from '../../store/slices/moviesSlice';
import dashboardReducer from '../../store/slices/dashboardSlice';
import MoviesList from '../MoviesList';

jest.mock('../../hooks', () => ({
  useMovies: () => ({
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
    loadMovies: jest.fn(),
    updateFilters: jest.fn(),
    changePage: jest.fn(),
    clearError: jest.fn(),
  }),
}));

const store = configureStore({
  reducer: {
    movies: moviesReducer,
    dashboard: dashboardReducer,
  },
});

describe('MoviesList Component', () => {
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
    expect(screen.getByText('Lista de filmes')).toBeInTheDocument();
  });

  it('deve renderizar os cabeçalhos da tabela', () => {
    renderMoviesList();
    
    expect(screen.getByText('ID')).toBeInTheDocument();
    expect(screen.getByText('Ano')).toBeInTheDocument();
    expect(screen.getByText('Título')).toBeInTheDocument();
    expect(screen.getByText('Vencedor?')).toBeInTheDocument();
  });

  it('deve renderizar os filmes na tabela', () => {
    renderMoviesList();
    
    expect(screen.getByText('Can\'t Stop the Music')).toBeInTheDocument();
    expect(screen.getByText('Mommie Dearest')).toBeInTheDocument();
    expect(screen.getByText('1980')).toBeInTheDocument();
    expect(screen.getByText('1981')).toBeInTheDocument();
    
    expect(screen.getAllByText('Sim').length).toBeGreaterThan(0);
    expect(screen.getAllByText('Não').length).toBeGreaterThan(0);
  });

  it('deve renderizar os campos de filtro', () => {
    renderMoviesList();
    
    const yearInput = screen.getByPlaceholderText('Filtrar por ano');
    expect(yearInput).toBeInTheDocument();
    
    const winnerSelect = screen.getByDisplayValue('Sim/Não');
    expect(winnerSelect).toBeInTheDocument();
  });
});
