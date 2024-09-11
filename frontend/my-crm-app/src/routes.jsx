import { Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import AdminDashboard from './pages/AdminDashboard';
import UserDashboard from './pages/UserDashboard';
import CreateCustomerPage from './pages/CreateCustomerPage';
import ListCustomersPage from './pages/ListCustomersPage';
import CreateUserPage from './pages/CreateUserPage';
import ListUsersPage from './pages/ListUsersPage';

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route path="/admin" element={<AdminDashboard />} />
            <Route path="/user" element={<UserDashboard />} />
            <Route path="/admin/create-user" element={<CreateUserPage />} />
            <Route path="/admin/list-users" element={<ListUsersPage />} />
            <Route path="/user/create-customer" element={<CreateCustomerPage />} />
            <Route path="/user/list-customers" element={<ListCustomersPage />} />
        </Routes>
    );
};

export default AppRoutes;
