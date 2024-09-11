import { useState } from 'react';
import { Form, Button, Container } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate();

    const handleLogin = () => {
        if ((email === 'my@email.com' && password === 'my.pass1') ||
            (email === 'admin@email.com' && password === 'admin.pass1')) {
            const role = email === 'admin@email.com' ? 'admin' : 'user';
            navigate(`/${role}`);
        } else {
            console.log('Invalid credentials');
        }
    };

    return (
        <Container className="mt-5">
            <Form>
                <Form.Group controlId="formBasicEmail">
                    <Form.Label>Email address</Form.Label>
                    <Form.Control
                        type="email"
                        placeholder="Enter email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </Form.Group>

                <Form.Group controlId="formBasicPassword">
                    <Form.Label>Password</Form.Label>
                    <Form.Control
                        type={showPassword ? 'text' : 'password'}
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </Form.Group>

                <Form.Group controlId="formBasicCheckbox">
                    <Form.Check
                        type="checkbox"
                        label="Show Password"
                        onChange={() => setShowPassword(!showPassword)}
                    />
                </Form.Group>

                <Button variant="primary" onClick={handleLogin}>
                    Login
                </Button>
            </Form>
        </Container>
    );
};

export default LoginPage;
