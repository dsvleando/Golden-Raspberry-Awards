// Re-exporta todos os tipos das APIs para manter compatibilidade
export * from './MoviesApiTypes';
export * from './DashboardApiTypes';
export * from './PaginationTypes';

// Estado raiz da aplicação
export interface RootState {
  movies: import('./MoviesApiTypes').MoviesState;
  dashboard: import('./DashboardApiTypes').DashboardState;
}
