export interface YearWithMultipleWinners {
  year: number;
  winnerCount: number;
}

export interface YearsWithMultipleWinnersResponse {
  years: YearWithMultipleWinners[];
}

export interface StudioWithWinCount {
  name: string;
  winCount: number;
}

export interface StudiosWithWinCountResponse {
  studios: StudioWithWinCount[];
}

export interface ProducerInterval {
  producer: string;
  interval: number;
  previousWin: number;
  followingWin: number;
}

export interface MaxMinWinIntervalResponse {
  min: ProducerInterval[];
  max: ProducerInterval[];
}

export interface WinnersByYearResponse {
  id: number;
  year: number;
  title: string;
  studios: string[];
  producers: string[];
  winner: boolean;
}

export interface DashboardState {
  yearsWithMultipleWinners: YearWithMultipleWinners[];
  studiosWithWinCount: StudioWithWinCount[];
  producerIntervals: MaxMinWinIntervalResponse;
  winnersByYear: WinnersByYearResponse[];
  selectedYear: number | null;
  loading: {
    yearsWithMultipleWinners: boolean;
    studiosWithWinCount: boolean;
    producerIntervals: boolean;
    winnersByYear: boolean;
  };
  error: string | null;
}
