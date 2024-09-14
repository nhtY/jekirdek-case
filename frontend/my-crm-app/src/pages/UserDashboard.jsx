import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar';

const UserDashboard = () => {
    return (
        <div className="row">
            <Sidebar />
            <div className='col-md-9 ms-sm-auto col-lg-10 px-md-4' >
                {/* <h2>User Dashboard</h2> */}
                {/* Main content*/}
                <Outlet />
            </div>
        </div>
    );
};

export default UserDashboard;
