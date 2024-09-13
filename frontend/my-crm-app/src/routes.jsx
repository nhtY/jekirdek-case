import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './hooks/useAuth';
import LoginPage from './pages/LoginPage';
import AdminDashboard from './pages/AdminDashboard';
import UserDashboard from './pages/UserDashboard';
import CreateUserPage from './pages/CreateUserPage';
import ListUsersPage from './pages/ListUsersPage';
import CreateCustomerPage from './pages/CreateCustomerPage';
import ListCustomersPage from './pages/ListCustomersPage';
import NotFoundPage from './pages/NotFoundPage';
import FilterCustomersWithSpecsPage from './pages/FilterCustomersWithSpecsPage';

// Protected Route Component
// eslint-disable-next-line react/prop-types
const PrivateRoute = ({ children, allowedRoles }) => {
    const { isLoggedIn, role } = useAuth();

    if (!isLoggedIn) {
        return <Navigate to="/" />;
    }

    // eslint-disable-next-line react/prop-types
    if (allowedRoles && !allowedRoles.includes(role)) {
        return <Navigate to="/" />;
    }

    return children;
};

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<LoginPage />} />

            {/* Protected Admin Routes */}
            <Route
                path="/admin"
                element={
                    <PrivateRoute allowedRoles={['ADMIN']}>
                        <AdminDashboard />
                    </PrivateRoute>
                }
            >
                <Route path="create-user" element={<CreateUserPage />} />
                <Route path="list-users" element={<ListUsersPage />} />
            </Route>

            {/* Protected User Routes */}
            <Route
                path="/user"
                element={
                    <PrivateRoute allowedRoles={['USER']}>
                        <UserDashboard />
                    </PrivateRoute>
                }
            >
                <Route path="create-customer" element={<CreateCustomerPage />} />
                <Route path="list-customers" element={<ListCustomersPage />} />
                <Route path="filter-customers-with-specs" element={<FilterCustomersWithSpecsPage />} />
            </Route>

            {/* 404 Not Found Route */}
            <Route path="*" element={<NotFoundPage />} />
        </Routes>
    );
};

export default AppRoutes;
