import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Navbar, Nav, Container, Button } from 'react-bootstrap';
import { useAuth } from '../hooks/useAuth';

const AppNavbar = () => {
    const { isLoggedIn, role, logout } = useAuth();
    const [expanded, setExpanded] = useState(false); // State to control collapse behavior

    return (
        <Navbar bg="dark" variant="dark" expand="lg" expanded={expanded} onToggle={() => setExpanded(!expanded)}>
            <Container>
                <Navbar.Brand as={Link} to="/">
                    CompanyName
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" onClick={() => setExpanded(!expanded)} />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        {isLoggedIn && role === 'ADMIN' && (
                            <>
                                <Nav.Link as={Link} to="/admin/create-user" onClick={() => setExpanded(false)}>Create User</Nav.Link>
                                <Nav.Link as={Link} to="/admin/list-users" onClick={() => setExpanded(false)}>List Users</Nav.Link>
                            </>
                        )}
                        {isLoggedIn && role === 'USER' && (
                            <>
                                <Nav.Link as={Link} to="/user/create-customer" onClick={() => setExpanded(false)}>Create Customer</Nav.Link>
                                <Nav.Link as={Link} to="/user/list-customers" onClick={() => setExpanded(false)}>List Customers</Nav.Link>
                            </>
                        )}
                    </Nav>
                    {isLoggedIn && (
                        <Button variant="outline-light" onClick={() => { setExpanded(false); logout(); }}>
                            Logout
                        </Button>
                    )}
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default AppNavbar;
