import { createContext, useState } from 'react';

// Create Auth Context
export const AuthContext = createContext(null);



// eslint-disable-next-line react/prop-types
const AuthProvider = ({ children }) => {
    const [authData, setAuthData] = useState({
        user: null,
        role: '',
        token: '',
        isLoggedIn: false,
    });

    // Login function
    const login = (user, role, token) => {
        setAuthData({
            user,
            role,
            token,
            isLoggedIn: true,
        });
    };

    // Logout function
    const logout = () => {
        setAuthData({
            user: null,
            role: '',
            token: '',
            isLoggedIn: false,
        });
    };

    return (
        <AuthContext.Provider value={{ ...authData, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;
