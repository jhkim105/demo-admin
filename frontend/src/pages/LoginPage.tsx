import { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import { login as loginApi } from "../api/auth";
import { useAuth } from "../AuthContext";

const LoginPage = () => {
  const [email, setEmail] = useState("admin@example.com");
  const [password, setPassword] = useState("admin123");
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const auth = useAuth();

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setError(null);
    try {
      const { token } = await loginApi(email, password);
      auth.login(token);
      navigate("/");
    } catch (e) {
      setError((e as Error).message);
    }
  };

  return (
    <section className="card narrow">
      <h2>로그인</h2>
      <form onSubmit={handleSubmit} className="stack">
        <label className="stack">
          <span>이메일</span>
          <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" required />
        </label>
        <label className="stack">
          <span>비밀번호</span>
          <input
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            type="password"
            required
          />
        </label>
        {error && <p className="error">{error}</p>}
        <button type="submit">로그인</button>
      </form>
    </section>
  );
};

export default LoginPage;
