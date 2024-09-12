// import reactLogo from './assets/react.svg'
// import viteLogo from '/vite.svg'
import './App.css'

import { BrowserRouter as Router } from 'react-router-dom';
import AppNavbar from './components/Navbar';
import Footer from './components/Footer';
import AppRoutes from './routes';
import AuthProvider from './context/AuthContext';

const App = () => {
    return (
        <AuthProvider>
            <Router>
                <div className="d-flex flex-column min-vh-100">
                    <AppNavbar />
                    <main className="vh-100">
                        <AppRoutes />
                    </main>
                    <Footer />
                </div>
            </Router>
        </AuthProvider>
    );
};

export default App;

