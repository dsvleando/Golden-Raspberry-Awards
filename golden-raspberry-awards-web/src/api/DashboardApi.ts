import { BaseApi } from './BaseApi';
import {
  YearsWithMultipleWinnersResponse,
  StudiosWithWinCountResponse,
  MaxMinWinIntervalResponse,
  WinnersByYearResponse,
} from '../types/DashboardApiTypes';

export class DashboardApi extends BaseApi {

  async getYearsWithMultipleWinners(): Promise<YearsWithMultipleWinnersResponse> {
    return this.get<YearsWithMultipleWinnersResponse>('/api/movies/yearsWithMultipleWinners');
  }

  async getStudiosWithWinCount(): Promise<StudiosWithWinCountResponse> {
    return this.get<StudiosWithWinCountResponse>('/api/movies/studiosWithWinCount');
  }

  async getProducerIntervals(): Promise<MaxMinWinIntervalResponse> {
    return this.get<MaxMinWinIntervalResponse>('/api/movies/maxMinWinIntervalForProducers');
  }

  async getWinnersByYear(year: number): Promise<WinnersByYearResponse[]> {
    return this.get<WinnersByYearResponse[]>(`/api/movies/winnersByYear`, { year });
  }
}
