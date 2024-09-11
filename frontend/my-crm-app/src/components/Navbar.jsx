import { Link } from 'react-router-dom';
import { Navbar, Nav, Container, Button } from 'react-bootstrap';
import { FaBars } from 'react-icons/fa';

const AppNavbar = () => {
    return (
        <Navbar bg="light" expand="lg">
            <Container>
                <Navbar.Brand href="#">CompanyName</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav">
                    <FaBars />
                </Navbar.Toggle>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="mr-auto">
                        <Nav.Link as={Link} to="/admin">Admin Dashboard</Nav.Link>
                        <Nav.Link as={Link} to="/user">User Dashboard</Nav.Link>
                        <Button variant="outline-danger" className="ml-auto">Logout</Button>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default AppNavbar;
