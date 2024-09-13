import { Link } from 'react-router-dom';
import { Nav, Navbar } from 'react-bootstrap';
import { useAuth } from '../hooks/useAuth'; // Import the custom hook
import { useEffect, useState } from 'react';

const Sidebar = () => {
    const { role, isLoggedIn } = useAuth(); // Get role and isLoggedIn status from AuthContext

    // get if screen is smaller than tablet view
    const [isTablet, setIsTablet] = useState(window.innerWidth > 768);

    useEffect(() => {
        const handleResize = () => {
            setIsTablet(window.innerWidth > 768);
        };

        window.addEventListener('resize', handleResize);

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    return (
        <>
            {isLoggedIn && isTablet && (  // Sidebar should only be visible for logged in users
                <Navbar bg="light" className="flex-column" expand="sm" style={{ minWidth: '180px', minHeight: '100%'}} >
                    <Navbar.Brand href="#">Dashboard</Navbar.Brand>
                    <Nav className="flex-column">
                        {role === 'ADMIN' && (
                            <>
                                <Nav.Link as={Link} to="/admin/create-user">Create User</Nav.Link>
                                <Nav.Link as={Link} to="/admin/list-users">List Users</Nav.Link>
                            </>
                        )}
                        {role === 'USER' && (
                            <>
                                <Nav.Link as={Link} to="/user/create-customer">Create Customer</Nav.Link>
                                <Nav.Link as={Link} to="/user/list-customers">List Customers</Nav.Link>
                                <Nav.Link as={Link} to="/user/filter-customers-with-specs">Filter With Specs</Nav.Link>
                            </>
                        )}
                    </Nav>
                    <div className='mt-auto'></div>
                </Navbar>
            )}
        </>
    );
};

export default Sidebar;
