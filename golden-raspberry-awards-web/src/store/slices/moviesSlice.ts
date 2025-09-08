import { createSlice, createAsyncThunk, PayloadAction } from "@reduxjs/toolkit";
import { Movie, moviesApi, MoviesFilters, MoviesState } from "../../api";
import { PaginatedResponse } from "../../types/PaginationTypes";

const initialState: MoviesState = {
  movies: {
    content: [],
    totalElements: 0,
    last: false,
    totalPages: 0,
    first: true,
    sort: {
      sorted: false,
      unsorted: true,
    },
    number: 0,
    numberOfElements: 0,
    size: 15,
  },
  loading: false,
  error: null,
  filters: {
    page: 0,
    size: 15,
  },
};

export const fetchMovies = createAsyncThunk<
  PaginatedResponse<Movie>,
  MoviesFilters
>("movies/fetchMovies", async (filters: MoviesFilters = {}) => {
  const response = await moviesApi.getMovies(filters);
  return response;
});

const moviesSlice = createSlice({
  name: "movies",
  initialState,
  reducers: {
    setFilters: (state, action: PayloadAction<Partial<MoviesFilters>>) => {
      state.filters = { ...state.filters, ...action.payload };
    },
    setPage: (state, action: PayloadAction<number>) => {
      state.filters.page = action.payload;
    },
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchMovies.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchMovies.fulfilled, (state, action) => {
        state.loading = false;
        state.movies = action.payload;
      })
      .addCase(fetchMovies.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || "Failed to load movies";
      });
  },
});

export const { setFilters, setPage, clearError } = moviesSlice.actions;
export default moviesSlice.reducer;
