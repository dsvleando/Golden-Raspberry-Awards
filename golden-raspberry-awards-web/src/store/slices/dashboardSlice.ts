import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { dashboardApi } from '../../api';
import { DashboardState } from '../../types/DashboardApiTypes';

const initialState: DashboardState = {
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
};

export const fetchYearsWithMultipleWinners = createAsyncThunk(
  'dashboard/fetchYearsWithMultipleWinners',
  async () => {
    const response = await dashboardApi.getYearsWithMultipleWinners();
    return response.years;
  }
);

export const fetchStudiosWithWinCount = createAsyncThunk(
  'dashboard/fetchStudiosWithWinCount',
  async () => {
    const response = await dashboardApi.getStudiosWithWinCount();
    return response.studios;
  }
);

export const fetchProducerIntervals = createAsyncThunk(
  'dashboard/fetchProducerIntervals',
  async () => {
    const response = await dashboardApi.getProducerIntervals();
    return response;
  }
);

export const fetchWinnersByYear = createAsyncThunk(
  'dashboard/fetchWinnersByYear',
  async (year: number) => {
    const response = await dashboardApi.getWinnersByYear(year);
    return response;
  }
);

const dashboardSlice = createSlice({
  name: 'dashboard',
  initialState,
  reducers: {
    setSelectedYear: (state, action: PayloadAction<number>) => {
      state.selectedYear = action.payload;
    },
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Years with multiple winners
      .addCase(fetchYearsWithMultipleWinners.pending, (state) => {
        state.loading.yearsWithMultipleWinners = true;
        state.error = null;
      })
      .addCase(fetchYearsWithMultipleWinners.fulfilled, (state, action) => {
        state.loading.yearsWithMultipleWinners = false;
        state.yearsWithMultipleWinners = action.payload;
      })
      .addCase(fetchYearsWithMultipleWinners.rejected, (state, action) => {
        state.loading.yearsWithMultipleWinners = false;
        state.error = action.error.message || 'Erro ao carregar anos com múltiplos vencedores';
      })
      // Studios with win count
      .addCase(fetchStudiosWithWinCount.pending, (state) => {
        state.loading.studiosWithWinCount = true;
        state.error = null;
      })
      .addCase(fetchStudiosWithWinCount.fulfilled, (state, action) => {
        state.loading.studiosWithWinCount = false;
        state.studiosWithWinCount = action.payload;
      })
      .addCase(fetchStudiosWithWinCount.rejected, (state, action) => {
        state.loading.studiosWithWinCount = false;
        state.error = action.error.message || 'Erro ao carregar estúdios com contagem de vitórias';
      })
      // Producer intervals
      .addCase(fetchProducerIntervals.pending, (state) => {
        state.loading.producerIntervals = true;
        state.error = null;
      })
      .addCase(fetchProducerIntervals.fulfilled, (state, action) => {
        state.loading.producerIntervals = false;
        state.producerIntervals = action.payload;
      })
      .addCase(fetchProducerIntervals.rejected, (state, action) => {
        state.loading.producerIntervals = false;
        state.error = action.error.message || 'Erro ao carregar intervalos de produtores';
      })
      // Winners by year
      .addCase(fetchWinnersByYear.pending, (state) => {
        state.loading.winnersByYear = true;
        state.error = null;
      })
      .addCase(fetchWinnersByYear.fulfilled, (state, action) => {
        state.loading.winnersByYear = false;
        state.winnersByYear = action.payload;
      })
      .addCase(fetchWinnersByYear.rejected, (state, action) => {
        state.loading.winnersByYear = false;
        state.error = action.error.message || 'Erro ao carregar vencedores por ano';
      });
  },
});

export const { setSelectedYear, clearError } = dashboardSlice.actions;
export default dashboardSlice.reducer;