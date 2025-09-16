jest.mock('../../api', () => ({
  moviesApi: {
    getMovies: jest.fn().mockResolvedValue({
      content: [
        { id: 1, year: 1980, title: 'Movie 1', winner: true },
        { id: 2, year: 1981, title: 'Movie 2', winner: false },
        { id: 3, year: 1982, title: 'Movie 3', winner: true }
      ],
      totalElements: 3,
      totalPages: 1,
      number: 0,
      size: 20
    })
  }
}));

import React from 'react';
import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MantineProvider } from '@mantine/core';
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

const renderMoviesList = (store = createTestStore()) => {
  return render(
    <Provider store={store}>
      <MantineProvider>
        <MoviesList />
      </MantineProvider>
    </Provider>
  );
};

describe('MoviesList Integration Tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('deve renderizar a lista de filmes sem erros', () => {
    renderMoviesList();

    expect(screen.getByText('List movies')).toBeInTheDocument();

    expect(screen.getByText('Filters')).toBeInTheDocument();
    expect(screen.getByLabelText('Year')).toBeInTheDocument();
    expect(screen.getAllByLabelText('Winner?').length).toBeGreaterThan(0);
    expect(screen.getByText('Search')).toBeInTheDocument();
  });

  it('deve exibir estrutura da tabela de filmes', () => {
    renderMoviesList();

    expect(screen.getByText('ID')).toBeInTheDocument();
    expect(screen.getAllByText('Year').length).toBeGreaterThan(0);
    expect(screen.getByText('Title')).toBeInTheDocument();
    expect(screen.getAllByText('Winner?').length).toBeGreaterThan(0);
  });

  it('deve exibir opções de filtro de vencedor', () => {
    renderMoviesList();

    const winnerSelect = screen.getByDisplayValue('Yes/No');
    expect(winnerSelect).toBeInTheDocument();
  });

  it('deve exibir campo de filtro por ano', () => {
    renderMoviesList();

    expect(screen.getByPlaceholderText('Filter by year')).toBeInTheDocument();
  });

  it('deve exibir botão de busca', () => {
    renderMoviesList();

    expect(screen.getByText('Search')).toBeInTheDocument();
  });
});
