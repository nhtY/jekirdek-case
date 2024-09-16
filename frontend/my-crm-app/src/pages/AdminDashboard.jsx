import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar';

const AdminDashboard = () => {
    return (
        <div className="row min-vh-100">
            <Sidebar />
            <div className='col-md-9 ms-sm-auto col-lg-10 px-md-4'>
                {/* <h2>Admin Dashboard</h2> */}
                {/* Main content*/}
                <Outlet />
            </div>
        </div>
    );
};

export default AdminDashboard;
