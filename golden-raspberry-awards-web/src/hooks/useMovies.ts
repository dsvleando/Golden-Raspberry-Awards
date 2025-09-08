import { useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState, AppDispatch } from "../store";
import {
  fetchMovies,
  setFilters,
  setPage,
  clearError,
} from "../store/slices/moviesSlice";
import { MoviesFilters } from "../types/MoviesApiTypes";

export const useMovies = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { movies, loading, filters, error } = useSelector(
    (state: RootState) => state.movies
  );

  const loadMovies = useCallback(
    (filters: MoviesFilters = {}) => {
      dispatch(fetchMovies(filters));
    },
    [dispatch]
  );

  const updateFilters = useCallback(
    (filters: Partial<MoviesFilters>) => {
      dispatch(setFilters(filters));
    },
    [dispatch]
  );

  const changePage = useCallback(
    (page: number) => {
      dispatch(setPage(page));
    },
    [dispatch]
  );

  const clearMoviesError = useCallback(() => {
    dispatch(clearError());
  }, [dispatch]);

  return {
    movies,
    loading,
    filters,
    error,
    loadMovies,
    updateFilters,
    changePage,
    clearError: clearMoviesError,
  };
};
