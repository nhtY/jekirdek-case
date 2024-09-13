import { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import axios from "../axios/axios";

const CreateUserPage = () => {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    username: "",
    email: "",
    password: "",
    roleName: "", // New field for role selection
  });
  const [roles, setRoles] = useState([]); // To store roles from the API
  const [errors, setErrors] = useState({});
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  // Fetch roles from API
  useEffect(() => {
    const fetchRoles = async () => {
      try {
        const response = await axios.get("/roles");
        setRoles(response.data); // Assuming the API returns a list of strings
      } catch (error) {
        console.log("Error fetching roles: ", error);
      }
    };

    fetchRoles();
  }, []);

  const validateForm = () => {
    let newErrors = {};

    // Check that string fields don't contain numbers
    const nameRegex = /^[a-zA-Z\s]+$/;

    if (!formData.firstName.trim() || formData.firstName.length < 3 || !nameRegex.test(formData.firstName)) {
      newErrors.firstName = "First Name is required and should contain at least 3 alphabetic characters.";
    }

    if (!formData.lastName.trim() || formData.lastName.length < 3 || !nameRegex.test(formData.lastName)) {
      newErrors.lastName = "Last Name is required and should contain at least 3 alphabetic characters.";
    }

    if (!formData.username.trim() || formData.username.length < 3) {
      newErrors.username = "Username is required and should contain at least 3 characters.";
    }

    if (!formData.email.trim()) {
      newErrors.email = "Email is required.";
    }

    if (!formData.password.trim() || formData.password.length < 6) {
      newErrors.password = "Password must be at least 6 characters.";
    }

    if (!formData.roleName) {
      newErrors.roleName = "Role selection is required.";
    }

    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate form data
    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    try {
      const response = await axios.post("/users", formData);


      if (!(response.status >= 200 && response.status < 300)) {
        setErrorMessage(response.data.message || "An error occurred");
        setShowErrorModal(true);
      } else {
        setShowSuccessModal(true);
        setFormData({
          firstName: "",
          lastName: "",
          username: "",
          email: "",
          password: "",
          roleName: "",
        });
      }
    } catch (error) {
      console.log("CREATE USER ERROR: ", error);
      setErrorMessage(error.response.data.message || "Something went wrong. Please try again later.");
      setShowErrorModal(true);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setErrors({ ...errors, [name]: "" });
  };

  return (
    <div className="d-flex justify-content-center align-items-center min-vh-100">
      <div className="card shadow-lg rounded" style={{ width: "40%", padding: "2rem", backgroundColor: "#f9f9f9" }}>
        <h3 className="text-center mb-4">Create User</h3>
        <Form onSubmit={handleSubmit}>
          <Form.Group controlId="firstName">
            <Form.Label>First Name</Form.Label>
            <Form.Control
              type="text"
              name="firstName"
              value={formData.firstName}
              onChange={handleInputChange}
              isInvalid={!!errors.firstName}
            />
            <Form.Control.Feedback type="invalid">{errors.firstName}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="lastName">
            <Form.Label>Last Name</Form.Label>
            <Form.Control
              type="text"
              name="lastName"
              value={formData.lastName}
              onChange={handleInputChange}
              isInvalid={!!errors.lastName}
            />
            <Form.Control.Feedback type="invalid">{errors.lastName}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="username">
            <Form.Label>Username</Form.Label>
            <Form.Control
              type="text"
              name="username"
              value={formData.username}
              onChange={handleInputChange}
              isInvalid={!!errors.username}
            />
            <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="email">
            <Form.Label>Email</Form.Label>
            <Form.Control
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              isInvalid={!!errors.email}
            />
            <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="password">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              name="password"
              value={formData.password}
              onChange={handleInputChange}
              isInvalid={!!errors.password}
            />
            <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="roleName">
            <Form.Label>Role</Form.Label>
            <Form.Control
              as="select"
              name="roleName"
              value={formData.roleName}
              onChange={handleInputChange}
              isInvalid={!!errors.roleName}
            >
              <option value="">Select a role</option>
              {roles.map((role, index) => (
                <option key={index} value={role}>
                  {role}
                </option>
              ))}
            </Form.Control>
            <Form.Control.Feedback type="invalid">{errors.roleName}</Form.Control.Feedback>
          </Form.Group>

          <Button className="mt-4 w-100" type="submit" variant="primary">
            Create User
          </Button>
        </Form>
      </div>

      {/* Success Modal */}
      <Modal show={showSuccessModal} onHide={() => setShowSuccessModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Success</Modal.Title>
        </Modal.Header>
        <Modal.Body>User has been created successfully!</Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={() => setShowSuccessModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Error Modal */}
      <Modal show={showErrorModal} onHide={() => setShowErrorModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Error</Modal.Title>
        </Modal.Header>
        <Modal.Body>{errorMessage}</Modal.Body>
        <Modal.Footer>
          <Button variant="danger" onClick={() => setShowErrorModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default CreateUserPage;
