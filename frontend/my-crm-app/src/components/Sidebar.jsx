import { Link } from 'react-router-dom';
import { Nav, Navbar } from 'react-bootstrap';

const Sidebar = () => {
    return (
        <Navbar bg="light" className="flex-column" expand="lg" style={{ width: '250px' }}>
            <Navbar.Brand href="#">Dashboard</Navbar.Brand>
            <Nav className="flex-column">
                <Nav.Link as={Link} to="/admin/create-user">Create User</Nav.Link>
                <Nav.Link as={Link} to="/admin/list-users">List Users</Nav.Link>
                <Nav.Link as={Link} to="/user/create-customer">Create Customer</Nav.Link>
                <Nav.Link as={Link} to="/user/list-customers">List Customers</Nav.Link>
            </Nav>
        </Navbar>
    );
};

export default Sidebar;
