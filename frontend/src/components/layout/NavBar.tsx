import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../auth/AuthContext';

export function NavBar() {
  const { username, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <Link to="/" className="brand">
        COC Character Sheets
      </Link>
      {isAuthenticated && (
        <div className="navbar-actions">
          <span className="navbar-username">{username}</span>
          <button type="button" className="btn btn-outline btn-small" onClick={handleLogout}>
            Log Out
          </button>
        </div>
      )}
    </nav>
  );
}
