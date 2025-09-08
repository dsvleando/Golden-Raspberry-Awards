import { useState, useCallback } from 'react';
import { useDashboard } from './useDashboard';

export const useWinnersSearch = () => {
  const { loadWinnersByYear, selectYear, winnersByYear, loading, error } = useDashboard();
  
  const [searchYear, setSearchYear] = useState<number | ''>('');

  const handleYearChange = useCallback((value: string | number) => {
    setSearchYear(typeof value === 'number' ? value : '');
  }, []);

  const handleYearBlur = useCallback(() => {
    if (searchYear) {
      selectYear(searchYear);
      loadWinnersByYear(searchYear);
    }
  }, [searchYear, selectYear, loadWinnersByYear]);

  return {
    searchYear,
    winnersByYear,
    loading: loading.winnersByYear,
    error,
    
    handleYearChange,
    handleYearBlur,
  };
};
