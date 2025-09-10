import { useState, useCallback } from "react";
import { useDashboard } from "./useDashboard";

export const useWinnersSearch = () => {
  const { loadWinnersByYear, selectYear, winnersByYear, loading, error } =
    useDashboard();

  const [searchYear, setSearchYear] = useState<string>("");

  const handleYearChange = useCallback((value: string) => {
    setSearchYear(value);
  }, []);

  const handleSearch = useCallback(() => {
    if (searchYear) {
      try {
        const year = parseInt(searchYear);
        if (isNaN(year)) {
          throw new Error("Ano inválido");
        }
        selectYear(year);
        loadWinnersByYear(year);
      } catch (error) {
        console.error("Erro ao buscar vencedores por ano:", error);
      }
    }
  }, [searchYear, selectYear, loadWinnersByYear]);

  return {
    searchYear,
    winnersByYear,
    loading: loading.winnersByYear,
    error,
    handleYearChange,
    handleSearch,
  };
};
