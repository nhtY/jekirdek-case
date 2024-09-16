
import { useAuth } from "../hooks/useAuth";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from "@fortawesome/free-solid-svg-icons";

const AppNavbar = () => {
  const { isLoggedIn, user, role } = useAuth();
  console.log("IS LOGGED IN? ", isLoggedIn);


  return (
    <header
      className="navbar sticky-top bg-dark flex-md-nowrap p-0 shadow"
      data-bs-theme="dark"
    >
      <a
        className="navbar-brand col-md-3 col-lg-2 me-0 px-3 fs-6 text-white"
        href="#"
      >
        Company name
      </a>

      {isLoggedIn && (
        <ul className="navbar-nav flex-row d-md-none">
          <li className="nav-item text-nowrap">
            <button
              className="nav-link px-3 text-white"
              type="button"
              data-bs-toggle="offcanvas"
              data-bs-target="#sidebarMenu"
              aria-controls="sidebarMenu"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <FontAwesomeIcon icon={faBars} />
            </button>
          </li>
        </ul>
      )}
    </header>
  );
};

export default AppNavbar;
