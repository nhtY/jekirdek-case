import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
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
            alert('Invalid credentials');
        }
    };

    return (
        <div className="container mt-5">
            <h2>Login</h2>
            <div className="form-group">
                <label>Email</label>
                <input
                    type="email"
                    className="form-control"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
            </div>
            <div className="form-group">
                <label>Password</label>
                <input
                    type={showPassword ? 'text' : 'password'}
                    className="form-control"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <input
                    type="checkbox"
                    checked={showPassword}
                    onChange={() => setShowPassword(!showPassword)}
                />{' '}
                Show Password
            </div>
            <button className="btn btn-primary mt-3" onClick={handleLogin}>
                Login
            </button>
        </div>
    );
};

export default LoginPage;
