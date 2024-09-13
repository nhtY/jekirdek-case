import { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import axios from "../axios/axios";

const CreateCustomerPage = () => {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    region: "",
  });
  const [errors, setErrors] = useState({});
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const validateForm = () => {
    let newErrors = {};
    const nameRegex = /^[a-zA-Z\s]+$/;

    if (!formData.firstName.trim() || formData.firstName.length < 3 || !nameRegex.test(formData.firstName)) {
      newErrors.firstName = "First Name is required and should contain at least 3 alphabetic characters.";
    }

    if (!formData.lastName.trim() || formData.lastName.length < 3 || !nameRegex.test(formData.lastName)) {
      newErrors.lastName = "Last Name is required and should contain at least 3 alphabetic characters.";
    }

    if (!formData.email.trim()) {
      newErrors.email = "Email is required.";
    }

    if (!formData.region) {
      newErrors.region = "Region selection is required.";
    }

    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    try {
      const response = await axios.post("/customers", formData);

      if (!(response.status >= 200 && response.status < 300)) {
        setErrorMessage(response.data.message || "An error occurred");
        setShowErrorModal(true);
      } else {
        setShowSuccessModal(true);
        setFormData({
          firstName: "",
          lastName: "",
          email: "",
          region: "",
        });
      }
    } catch (error) {
      console.log("CREATE CUSTOMER ERROR: ", error);
      setErrorMessage(error.response?.data?.message || "Something went wrong. Please try again later.");
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
        <h3 className="text-center mb-4">Create Customer</h3>
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

          <Form.Group controlId="region">
            <Form.Label>Region</Form.Label>
            <Form.Control
              as="select"
              name="region"
              value={formData.region}
              onChange={handleInputChange}
              isInvalid={!!errors.region}
            >
              <option value="">Select a region</option>
              <option value="A">A</option>
              <option value="B">B</option>
              <option value="C">C</option>
              <option value="D">D</option>
            </Form.Control>
            <Form.Control.Feedback type="invalid">{errors.region}</Form.Control.Feedback>
          </Form.Group>

          <Button className="mt-4 w-100" type="submit" variant="primary">
            Create Customer
          </Button>
        </Form>
      </div>

      <Modal show={showSuccessModal} onHide={() => setShowSuccessModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Success</Modal.Title>
        </Modal.Header>
        <Modal.Body>Customer has been created successfully!</Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={() => setShowSuccessModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>

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

export default CreateCustomerPage;
