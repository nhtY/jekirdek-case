// import reactLogo from './assets/react.svg'
// import viteLogo from '/vite.svg'
import "./App.css";

import { BrowserRouter as Router } from "react-router-dom";
import AppNavbar from "./components/Navbar";
import Footer from "./components/Footer";
import AppRoutes from "./routes";
import AuthProvider from "./context/AuthContext";
import { ErrorProvider } from "./context/ErrorContext";
import ErrorModal from "./components/ErrorModal";

const App = () => {
  return (
    <ErrorProvider>
      <Router>
        <AuthProvider>
          <AppNavbar />
          <div className="container-fluid">
            <AppRoutes />
          </div>
          <footer className="">
            <Footer />
          </footer>
          <ErrorModal />
        </AuthProvider>
      </Router>
    </ErrorProvider>
  );
};

export default App;
