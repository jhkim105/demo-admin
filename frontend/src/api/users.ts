import { ApiUser, PagedUsers, Role, UserPayload } from "../types";
import { request } from "./client";

export const fetchUsers = async (page = 0, size = 10): Promise<PagedUsers> =>
  request<PagedUsers>(`/users?page=${page}&size=${size}`);

export const fetchUser = async (id: number): Promise<ApiUser> => request<ApiUser>(`/users/${id}`);

export const fetchRoles = async (): Promise<Role[]> => request<Role[]>("/roles");

export const createUser = async (payload: UserPayload): Promise<ApiUser> =>
  request<ApiUser>("/users", { method: "POST", body: JSON.stringify(payload) });

export const updateUser = async (id: number, payload: UserPayload): Promise<ApiUser> =>
  request<ApiUser>(`/users/${id}`, { method: "PUT", body: JSON.stringify(payload) });

export const deleteUser = async (id: number): Promise<void> =>
  request<void>(`/users/${id}`, { method: "DELETE" });
