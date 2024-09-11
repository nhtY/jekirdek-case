import { Link } from 'react-router-dom';
import { Nav, Navbar } from 'react-bootstrap';
import { useAuth } from '../hooks/useAuth'; // Import the custom hook

const Sidebar = () => {
    const { role, isLoggedIn } = useAuth(); // Get role and isLoggedIn status from AuthContext

    return (
        <>
            {isLoggedIn && (  // Sidebar should only be visible for logged in users
                <Navbar bg="light" className="flex-column" expand="lg" style={{ width: '250px' }}>
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
                            </>
                        )}
                    </Nav>
                </Navbar>
            )}
        </>
    );
};

export default Sidebar;
