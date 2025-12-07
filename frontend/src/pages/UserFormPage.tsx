import { FormEvent, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createUser, fetchRoles, fetchUser, updateUser } from "../api/users";
import { Role } from "../types";

const UserFormPage = () => {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [roles, setRoles] = useState<string[]>(["ROLE_USER"]);
  const [availableRoles, setAvailableRoles] = useState<Role[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchRoles().then(setAvailableRoles).catch((e) => setError((e as Error).message));
  }, []);

  useEffect(() => {
    if (isEdit && id) {
      fetchUser(Number(id))
        .then((user) => {
          setEmail(user.email);
          setName(user.name);
          setRoles(user.roles);
        })
        .catch((e) => setError((e as Error).message));
    }
  }, [id, isEdit]);

  const toggleRole = (role: string) => {
    setRoles((prev) => (prev.includes(role) ? prev.filter((r) => r !== role) : [...prev, role]));
  };

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setError(null);
    try {
      if (isEdit && id) {
        await updateUser(Number(id), { email, name, password: password || undefined, roles });
      } else {
        await createUser({ email, name, password, roles });
      }
      navigate("/");
    } catch (e) {
      setError((e as Error).message);
    }
  };

  return (
    <section className="card">
      <div className="card-header">
        <h2>{isEdit ? "사용자 수정" : "새 사용자"}</h2>
        <button className="ghost" onClick={() => navigate("/")}>
          목록으로
        </button>
      </div>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit} className="stack">
        <label className="stack">
          <span>이메일</span>
          <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" required />
        </label>
        <label className="stack">
          <span>이름</span>
          <input value={name} onChange={(e) => setName(e.target.value)} required />
        </label>
        <label className="stack">
          <span>비밀번호 {isEdit && "(미입력 시 변경 없음)"}</span>
          <input
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            type="password"
            placeholder={isEdit ? "새 비밀번호" : ""}
            required={!isEdit}
          />
        </label>
        <div className="stack">
          <span>역할</span>
          <div className="pill-group">
            {availableRoles.map((role) => (
              <label key={role.id} className={roles.includes(role.name) ? "pill active" : "pill"}>
                <input
                  type="checkbox"
                  checked={roles.includes(role.name)}
                  onChange={() => toggleRole(role.name)}
                />
                {role.name}
              </label>
            ))}
          </div>
        </div>
        <button type="submit">{isEdit ? "수정" : "생성"}</button>
      </form>
    </section>
  );
};

export default UserFormPage;
