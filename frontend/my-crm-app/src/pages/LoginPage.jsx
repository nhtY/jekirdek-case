import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import axios from 'axios';
import NotificationModal from '../components/NotificationModal';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [modalProps, setModalProps] = useState({ show: false, message: '', type: 'info' }); // State to control modal visibility and content
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const response = await axios.post('/auth/login', {
                email,
                password,
            });

            // If login is successful, call login function in AuthContext
            if (response.status === 200) {
                // Fetch current user after successful login
                const currentUserResponse = await axios.get('/auth/current-user');
                const { username, authorities } = currentUserResponse.data;
                console.log("current response: ", username, authorities)

                login(username, authorities[0]); // Update context with user info

                // Redirect to the appropriate dashboard
                navigate(authorities[0] === 'ROLE_ADMIN' ? '/admin/list-users' : '/user/filter-customers-with-stream');
            }
        } catch (error) {
            console.log("login error: ", error)
            const message = error.response? error.response.data.message : error.message
            setModalProps({
                show: true,
                message: message || 'Invalid credentials. Please try again.',
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
