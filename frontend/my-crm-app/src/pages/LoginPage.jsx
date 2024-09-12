import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import NotificationModal from '../components/NotificationModal';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [modalProps, setModalProps] = useState({ show: false, message: '', type: 'info' }); // State to control modal visibility and content
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleLogin = () => {
        if (email === 'admin@email.com' && password === 'admin.pass1') {
            login({ email }, 'ADMIN', 'admin-token');
            navigate('/admin');
        } else if (email === 'user@email.com' && password === 'user.pass1') {
            login({ email }, 'USER', 'user-token');
            navigate('/user');
        } else {
            setModalProps({
                show: true,
                message: 'Invalid credentials. Please try again.',
                type: 'error'
            });
        }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center min-vh-100">
            <div className="card p-4 shadow-sm rounded" style={{ maxWidth: '400px', width: '100%' }}>
                <div className="card-body">
                    <h2 className="card-title text-center mb-4">Login</h2>
                    <form>
                        <div className="mb-3">
                            <label htmlFor="email" className="form-label">Email</label>
                            <input
                                type="email"
                                id="email"
                                className="form-control"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="password" className="form-label">Password</label>
                            <input
                                type={showPassword ? 'text' : 'password'}
                                id="password"
                                className="form-control"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                            <div className="form-check mt-2">
                                <input
                                    type="checkbox"
                                    id="showPassword"
                                    className="form-check-input"
                                    checked={showPassword}
                                    onChange={() => setShowPassword(!showPassword)}
                                />
                                <label htmlFor="showPassword" className="form-check-label">Show Password</label>
                            </div>
                        </div>
                        <button
                            type="button"
                            className="btn btn-primary w-100"
                            onClick={handleLogin}
                        >
                            Login
                        </button>
                    </form>
                </div>
            </div>

            {/* Use NotificationModal */}
            <NotificationModal
                show={modalProps.show}
                onClose={() => setModalProps({ ...modalProps, show: false })}
                message={modalProps.message}
                type={modalProps.type}
            />
        </div>
    );
};

export default LoginPage;
