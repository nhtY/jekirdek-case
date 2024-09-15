import { createContext, useEffect, useState } from 'react';
import axios from '../axios/axios'; // Update this with your actual axios instance path
import { useNavigate } from 'react-router-dom';

// Create Auth Context
export const AuthContext = createContext(null);

const AuthProvider = ({ children }) => {
    const [authData, setAuthData] = useState({
        user: null,
        role: '',
        isLoggedIn: false,
    });
    const navigate = useNavigate();

    // Fetch current user from backend when app loads
    useEffect(() => {
        const fetchCurrentUser = async () => {
            try {
                const response = await axios.get('/auth/current-user');
                setAuthData({
                    user: response.data.username,
                    role: response.data.authorities[0],
                    isLoggedIn: true,
                });
            } catch (error) {
                setAuthData({
                    user: null,
                    role: '',
                    isLoggedIn: false,
                });
                navigate('/'); // Navigate to login page if not authenticated
            }
        };
        fetchCurrentUser();
    }, [navigate]);

    // Login function
    const login = (user, role) => {
        setAuthData({
            user,
            role,
            isLoggedIn: true,
        });
    };

    // Logout function
    const logout = async () => {
        try {
            await axios.post('/auth/logout');
            setAuthData({
                user: null,
                role: '',
                isLoggedIn: false,
            });
            navigate('/');
        } catch (error) {
            console.error('Failed to logout:', error);
        }
    };

    return (
        <AuthContext.Provider value={{ ...authData, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;
