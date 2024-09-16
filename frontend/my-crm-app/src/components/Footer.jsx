import { Container, Navbar } from 'react-bootstrap';

const Footer = () => {
    return (
        <Navbar bg="light">
            <Container>
                <Navbar.Text>&copy; 2024 CompanyName</Navbar.Text>
            </Container>
        </Navbar>
    );
};

export default Footer;
