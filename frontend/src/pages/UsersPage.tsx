import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { deleteUser, fetchUsers } from "../api/users";
import { ApiUser } from "../types";

const UsersPage = () => {
  const [users, setUsers] = useState<ApiUser[]>([]);
  const [page, setPage] = useState(0);
  const [size] = useState(10);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const load = async (pageIndex: number) => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchUsers(pageIndex, size);
      setUsers(data.content);
      setTotal(data.total);
      setPage(pageIndex);
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load(0);
  }, []);

  const totalPages = Math.ceil(total / size) || 1;

  const handleDelete = async (id: number) => {
    if (!confirm("정말 삭제하시겠습니까?")) return;
    try {
      await deleteUser(id);
      load(page);
    } catch (e) {
      alert((e as Error).message);
    }
  };

  return (
    <section className="card">
      <div className="card-header">
        <h2>사용자 목록</h2>
        <button onClick={() => navigate("/users/new")}>새 사용자</button>
      </div>
      {error && <p className="error">{error}</p>}
      {loading ? (
        <p>로딩 중...</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>이메일</th>
              <th>이름</th>
              <th>역할</th>
              <th>액션</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.email}</td>
                <td>{user.name}</td>
                <td>{user.roles.join(", ")}</td>
                <td className="actions">
                  <button className="ghost" onClick={() => navigate(`/users/${user.id}`)}>
                    수정
                  </button>
                  <button className="danger ghost" onClick={() => handleDelete(user.id)}>
                    삭제
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      <div className="pagination">
        <button disabled={page === 0} onClick={() => load(page - 1)}>
          이전
        </button>
        <span>
          {page + 1} / {totalPages}
        </span>
        <button disabled={page + 1 >= totalPages} onClick={() => load(page + 1)}>
          다음
        </button>
      </div>
    </section>
  );
};

export default UsersPage;
