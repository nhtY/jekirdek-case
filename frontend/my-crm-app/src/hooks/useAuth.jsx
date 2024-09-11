import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

// Custom hook to use the Auth Context
export const useAuth = () => useContext(AuthContext);