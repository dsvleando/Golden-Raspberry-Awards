import { MoviesApi } from './MoviesApi';
import { DashboardApi } from './DashboardApi';

export { BaseApi } from './BaseApi';
export { MoviesApi } from './MoviesApi';
export { DashboardApi } from './DashboardApi';

export * from '../types/MoviesApiTypes';
export * from '../types/DashboardApiTypes';

export const moviesApi = new MoviesApi();
export const dashboardApi = new DashboardApi();