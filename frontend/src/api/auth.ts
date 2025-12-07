import { LoginResponse } from "../types";
import { request } from "./client";

export const login = async (email: string, password: string): Promise<LoginResponse> =>
  request<LoginResponse>("/auth/login", {
    method: "POST",
    body: JSON.stringify({ email, password })
  });
