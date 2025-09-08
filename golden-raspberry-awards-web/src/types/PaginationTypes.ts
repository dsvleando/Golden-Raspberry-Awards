export interface PaginatedRequest {
  page?: number;
  size?: number;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  last: boolean;
  totalPages: number;
  first: boolean;
  sort: {
    sorted: boolean;
    unsorted: boolean;
  };
  number: number;
  numberOfElements: number;
  size: number;
}
