import { Nav } from "react-bootstrap";
import { useAuth } from "../hooks/useAuth"; // Import the custom hook
import { useEffect } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faFilter,
  faList,
  faPlusCircle,
  faSignOut,
} from "@fortawesome/free-solid-svg-icons";
import { Link } from "react-router-dom";

const Sidebar = () => {
  const { role, isLoggedIn, user, logout } = useAuth(); // Get role and isLoggedIn status from AuthContext
  console.log(role, isLoggedIn);

  const handleLogout = () => {
    logout();
  };

  useEffect(() => {
    const handleResize = () => {
      // setIsTablet(window.innerWidth > 768);
    };

    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  return (
    <div className="sidebar border border-right col-md-3 col-lg-2 p-0 bg-body-tertiary">
      <div
        className="offcanvas-md offcanvas-end bg-body-tertiary"
        tabIndex="-1"
        id="sidebarMenu"
        aria-labelledby="sidebarMenuLabel"
      >
        <div className="offcanvas-header">
          <h5 className="offcanvas-title" id="sidebarMenuLabel">
            Company name
          </h5>
          <button
            type="button"
            className="btn-close"
            data-bs-dismiss="offcanvas"
            data-bs-target="#sidebarMenu"
            aria-label="Close"
          ></button>
        </div>
        <div className="offcanvas-body d-md-flex flex-column p-0 pt-lg-3 overflow-y-auto">
          <ul className="nav flex-column">
            {role === "ROLE_ADMIN" && (
              <>
                <Nav.Link
                  as={Link}
                  to="/admin/create-user"
                  className="d-flex align-items-center gap-2"
                >
                  <FontAwesomeIcon icon={faPlusCircle} />
                  Create User
                </Nav.Link>
                <Nav.Link
                  as={Link}
                  to="/admin/list-users"
                  className="d-flex align-items-center gap-2"
                >
                  List Users
                </Nav.Link>
              </>
            )}
            {role === "ROLE_USER" && (
              <>
                <Nav.Link
                  as={Link}
                  to="/user/create-customer"
                  className="d-flex align-items-center gap-2"
                >
                  <FontAwesomeIcon icon={faPlusCircle} />
                  Create Customer
                </Nav.Link>
                <Nav.Link
                  as={Link}
                  to="/user/list-customers"
                  className="d-flex align-items-center gap-2"
                >
                  <FontAwesomeIcon icon={faList} />
                  List Customers
                </Nav.Link>
                <Nav.Link
                  as={Link}
                  to="/user/filter-customers-with-stream"
                  className="d-flex align-items-center gap-2"
                >
                  <FontAwesomeIcon icon={faFilter} />
                  Filter With Stream
                </Nav.Link>
                <Nav.Link
                  as={Link}
                  to="/user/filter-customers-with-specs"
                  className="d-flex align-items-center gap-2"
                >
                  <FontAwesomeIcon icon={faFilter} />
                  Filter With JPA Specs
                </Nav.Link>
              </>
            )}
            <hr className="my-1" />
            <li className="nav-item text-nowrap">
              <span className="nav-link text-dark px-3">{`${user}`}</span>
            </li>
            <hr className="my-1" />
            <li className="nav-item">
              <Nav.Link
                as={Link}
                className="d-flex align-items-center gap-2"
                onClick={() => handleLogout()}
              >
                <FontAwesomeIcon icon={faSignOut} />
                Logout
              </Nav.Link>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
