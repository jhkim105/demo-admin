import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../AuthContext";

type LayoutProps = {
  children: React.ReactNode;
};

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleBrandClick = () => navigate("/");

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="app-shell">
      <header className="app-header">
        <div
          className="branding"
          role="button"
          onClick={handleBrandClick}
          onKeyDown={(e) => (e.key === "Enter" || e.key === " ") && handleBrandClick()}
          tabIndex={0}
        >
          <span className="dot" aria-hidden />
          <div>
            <h1>Demo Admin</h1>
            <small>관리 콘솔</small>
          </div>
        </div>
        {isAuthenticated && (
          <button className="ghost" onClick={handleLogout}>
            로그아웃
          </button>
        )}
      </header>

      {isAuthenticated && (
        <nav className="app-nav">
          <NavLink to="/" end className={({ isActive }) => (isActive ? "active" : undefined)}>
            사용자
          </NavLink>
          <NavLink
            to="/users/new"
            className={({ isActive }) => (isActive ? "active" : undefined)}
          >
            새 사용자
          </NavLink>
        </nav>
      )}

      <main>{children}</main>
    </div>
  );
};

export default Layout;
