import { BaseApi } from './BaseApi';
import { Movie, MoviesFilters } from '../types/MoviesApiTypes';
import { PaginatedResponse } from '../types/PaginationTypes';

export class MoviesApi extends BaseApi {

  async getMovies(filters: MoviesFilters = {}): Promise<PaginatedResponse<Movie>> {
    const params = {
      page: filters.page || 0,
      size: filters.size || 20,
      ...(filters.winner !== undefined && { winner: filters.winner }),
      ...(filters.year !== undefined && { year: filters.year }),
    };

    return this.get<PaginatedResponse<Movie>>('/api/movies', params);
  }

  async getMovieById(id: number): Promise<Movie> {
    return this.get<Movie>(`/api/movies/${id}`);
  }

  async getWinners(filters: Omit<MoviesFilters, 'winner'> = {}): Promise<PaginatedResponse<Movie>> {
    return this.getMovies({ ...filters, winner: true });
  }

  async getMoviesByYear(year: number, filters: Omit<MoviesFilters, 'year'> = {}): Promise<PaginatedResponse<Movie>> {
    return this.getMovies({ ...filters, year });
  }
}
