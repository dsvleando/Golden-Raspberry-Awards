import { useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState, AppDispatch } from "../store";
import {
  fetchYearsWithMultipleWinners,
  fetchStudiosWithWinCount,
  fetchProducerIntervals,
  fetchWinnersByYear,
  setSelectedYear,
  clearError,
} from "../store/slices/dashboardSlice";

export const useDashboard = () => {
  const dispatch = useDispatch<AppDispatch>();
  const {
    yearsWithMultipleWinners,
    studiosWithWinCount,
    producerIntervals,
    winnersByYear,
    selectedYear,
    loading,
    error,
  } = useSelector((state: RootState) => state.dashboard);

  const loadYearsWithMultipleWinners = useCallback(() => {
    dispatch(fetchYearsWithMultipleWinners());
  }, [dispatch]);

  const loadStudiosWithWinCount = useCallback(() => {
    dispatch(fetchStudiosWithWinCount());
  }, [dispatch]);

  const loadProducerIntervals = useCallback(() => {
    dispatch(fetchProducerIntervals());
  }, [dispatch]);

  const loadWinnersByYear = useCallback(
    (year: number) => {
      dispatch(fetchWinnersByYear(year));
    },
    [dispatch]
  );

  const selectYear = useCallback(
    (year: number) => {
      dispatch(setSelectedYear(year));
    },
    [dispatch]
  );

  const clearDashboardError = useCallback(() => {
    dispatch(clearError());
  }, [dispatch]);

  const loadDashboardData = useCallback(() => {
    loadYearsWithMultipleWinners();
    loadStudiosWithWinCount();
    loadProducerIntervals();
  }, [
    loadYearsWithMultipleWinners,
    loadStudiosWithWinCount,
    loadProducerIntervals,
  ]);

  return {
    yearsWithMultipleWinners,
    studiosWithWinCount,
    producerIntervals,
    winnersByYear,
    selectedYear,
    loading,
    error,

    loadYearsWithMultipleWinners,
    loadStudiosWithWinCount,
    loadProducerIntervals,
    loadWinnersByYear,
    selectYear,
    clearError: clearDashboardError,
    loadDashboardData,
  };
};
