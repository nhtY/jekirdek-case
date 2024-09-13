import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar';

const AdminDashboard = () => {
    return (
        <div className="d-flex" style={{ minHeight: '100vh' }}>
            <Sidebar />
            <div className="container-fluid container-xxl" style={{minHeight: '100%'}}>
                <h2>Admin Dashboard</h2>
                {/* Main content*/}
                <Outlet />
            </div>
        </div>
    );
};

export default AdminDashboard;
