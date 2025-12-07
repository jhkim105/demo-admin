export interface ApiUser {
  id: number;
  email: string;
  name: string;
  roles: string[];
}

export interface Role {
  id: number;
  name: string;
}

export interface PagedUsers {
  content: ApiUser[];
  total: number;
  page: number;
  size: number;
}

export interface LoginResponse {
  token: string;
}

export interface UserPayload {
  email: string;
  name: string;
  password?: string;
  roles: string[];
}
