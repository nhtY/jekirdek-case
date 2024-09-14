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
                
                <AppNavbar />
                <div className='container-fluid'>
                    <AppRoutes />
                </div>
                <footer className='sticky-bottom'>
                    <Footer />
                </footer>
                {/* <div className="d-flex flex-column min-vh-100">
                    <AppNavbar />
                    <main className="vh-100">
                        <AppRoutes />
                        <Footer />
                    </main>
                    
                </div> */}
            </Router>
        </AuthProvider>
    );
};

export default App;

