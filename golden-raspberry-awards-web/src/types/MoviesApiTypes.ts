import { PaginatedResponse } from "./PaginationTypes";

export interface Movie {
  id: number;
  year: number;
  title: string;
  studios: string[];
  producers: string[];
  winner: boolean;
}

export interface MoviesFilters {
  page?: number;
  size?: number;
  winner?: boolean;
  year?: number;
}

export interface MoviesState {
  movies: PaginatedResponse<Movie>;
  loading: boolean;
  filters: MoviesFilters;
  error: string | null;
}
