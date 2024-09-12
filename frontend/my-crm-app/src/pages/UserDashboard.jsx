import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar';

const UserDashboard = () => {
    return (
        <div className="d-flex" style={{ minHeight: '100%' }}>
            <Sidebar />
            <div className="container-fluid container-xxl">
                <h2>User Dashboard</h2>
                {/* Main content*/}
                <Outlet />
            </div>
        </div>
    );
};

export default UserDashboard;
