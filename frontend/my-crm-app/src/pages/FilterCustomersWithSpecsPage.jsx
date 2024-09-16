
import { useEffect, useState } from "react";
import axios from "../axios/axios";
import { Modal, Button, Form } from "react-bootstrap";
import IconButtonWithToolTip from "../components/IconButtonWithToolTip";
import {
  faPencilAlt,
  faTrash,
  faCheck,
  faTimes,
} from "@fortawesome/free-solid-svg-icons";
import ConfirmationModal from "../components/ConfirmationModal";

const FilterCustomersWithSpecsPage = () => {
  const [customers, setCustomers] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [sort, setSort] = useState("");
  const [error, setError] = useState(null);
  const [editingCustomerId, setEditingCustomerId] = useState(null);
  const [deleteCustomerId, setDeleteCustomerId] = useState(null);
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    region: "",
    registrationDateStart: "",
  });
  const [filterData, setFilterData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    region: "",
    registrationDateStart: "",
    registrationDateEnd: ""
  });
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);

  const handleCloseErrorModal = () => setShowErrorModal(false);
  const handleCloseConfirmModal = () => setShowConfirmModal(false);

  const fetchCustomers = async (pageNumber, size, sort, filters) => {
    try {
      const response = await axios.get("/customers/filter/specs/page", {
        params: {
          page: pageNumber,
          size: size,
          sort: sort ? sort : [],
          ...filters
        },
      });
      setCustomers(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error("FETCH ERROR: ", error);
      setError("Failed to load customers.");
      setShowErrorModal(true);
    }
  };

  const handleEditClick = (customer) => {
    setEditingCustomerId(customer.id);
    setFormData({
      firstName: customer.firstName,
      lastName: customer.lastName,
      email: customer.email,
      region: customer.region,
    });
  };

  const handleCancelEdit = () => {
    setEditingCustomerId(null);
    setFormData({
      firstName: "",
      lastName: "",
      email: "",
      region: "",
    });
  };

  const handleSaveCustomer = async (id) => {
    try {
      const response = await axios.put(`/customers/${id}`, formData);
      const updatedCustomers = customers.map((customer) =>
        customer.id === id ? { ...customer, ...response.data } : customer
      );
      setCustomers(updatedCustomers);
      setEditingCustomerId(null);
    } catch (error) {
      console.log("UPDATE ERROR: ", error);
      setError(error.response.data.message || "Failed to update customer.");
      setShowErrorModal(true);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleInputChangeFilter = (e) => {
    const { name, value } = e.target;
    setFilterData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleDeleteClick = (id) => {
    setDeleteCustomerId(id);
    setShowConfirmModal(true);
  };

  const handleConfirmDelete = async () => {
    try {
      await axios.delete(`/customers/${deleteCustomerId}`);
      setCustomers(customers.filter((customer) => customer.id !== deleteCustomerId));
      setDeleteCustomerId(null);
      setShowConfirmModal(false);
    } catch (error) {
      console.log("DELETE ERR: ", error);
      setError("Failed to delete customer.");
      setShowErrorModal(true);
    }
  };

  const handleFilterSubmit = (e) => {
    e.preventDefault();
    fetchCustomers(page, pageSize, sort, filterData);
  };

  const handleFilterFormClear = () => {
    setFilterData(
        {
            firstName: "",
            lastName: "",
            email: "",
            region: "",
            registrationDateStart: "",
            registrationDateEnd: ""
          }
    )
  }

  useEffect(() => {
    fetchCustomers(page, pageSize, sort, filterData);
  }, [page, pageSize, sort]);

  return (
    <div className="">
      <h3>Filter Customers With Specifications</h3>
        <p>The backend uses Spring Data JPA's Specifications to build the SQL query for filtering and the filtering operations happens on <b>DB</b></p>
    

      {/* Filter Form */}
      <Form onSubmit={handleFilterSubmit} className="mb-3">
        <div className="row">
          <div className="col-md-3">
            <Form.Group controlId="firstName">
              <Form.Label>First Name</Form.Label>
              <Form.Control
                type="text"
                name="firstName"
                value={filterData.firstName}
                onChange={handleInputChangeFilter}
              />
            </Form.Group>
          </div>
          <div className="col-md-3">
            <Form.Group controlId="lastName">
              <Form.Label>Last Name</Form.Label>
              <Form.Control
                type="text"
                name="lastName"
                value={filterData.lastName}
                onChange={handleInputChangeFilter}
              />
            </Form.Group>
          </div>
          <div className="col-md-3">
            <Form.Group controlId="email">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="text"
                name="email"
                value={filterData.email}
                onChange={handleInputChangeFilter}
              />
            </Form.Group>
          </div>
          <div className="col-md-3">
            <Form.Group controlId="region">
              <Form.Label>Region</Form.Label>
              <Form.Control
                as="select"
                name="region"
                value={filterData.region}
                onChange={handleInputChangeFilter}
              >
                <option value="">All Regions</option>
                <option value="A">A</option>
                <option value="B">B</option>
                <option value="C">C</option>
                <option value="D">D</option>
              </Form.Control>
            </Form.Group>
          </div>
          <div className="col-md-3">
            <Form.Group controlId="registrationDateStart">
              <Form.Label>Registration Date Start</Form.Label>
              <Form.Control
                type="date"
                name="registrationDateStart"
                value={filterData.registrationDateStart}
                onChange={handleInputChangeFilter}
              />
            </Form.Group>
          </div>
          <div className="col-md-3">
            <Form.Group controlId="registrationDateEnd">
              <Form.Label>Registration Date End</Form.Label>
              <Form.Control
                type="date"
                name="registrationDateEnd"
                value={filterData.registrationDateEnd}
                onChange={handleInputChangeFilter}
              />
            </Form.Group>
          </div>
        </div>
        <Button variant="primary" type="submit" className="mt-3 me-2">
          Filter
        </Button>
        <Button variant="secondary" className="mt-3 me-2" onClick={() => handleFilterFormClear()}>
          Clear
        </Button>
      </Form>

      <div className="container">
      <div className="row mb-3">
        <div className="col">
          <label>Page Size: </label>
          <select
            value={pageSize}
            onChange={(e) => setPageSize(e.target.value)}
            className="form-control w-auto"
          >
            <option value="10">10</option>
            <option value="20">20</option>
            <option value="30">30</option>
          </select>
        </div>

        <div className="col">
          <label>Sort By: </label>
          <select
            value={sort}
            onChange={(e) => setSort(e.target.value)}
            className="form-control w-auto"
          >
            <option value="">None</option>
            <option value="firstName,asc">First Name Ascending</option>
            <option value="firstName,desc">First Name Descending</option>
            <option value="lastName,asc">Last Name Ascending</option>
            <option value="lastName,desc">Last Name Descending</option>
            <option value="region,asc">Region Ascending</option>
            <option value="region,desc">Region Descending</option>
          </select>
        </div>
      </div>
      </div>

      <div className="table-responsive">
        <table className="table table-sm table-striped table-bordered table-hover">
          <thead className="table-dark">
            <tr>
              <th scope="col">ID</th>
              <th scope="col">First Name</th>
              <th scope="col">Last Name</th>
              <th scope="col">Email</th>
              <th scope="col">Region</th>
              <th scope="col">Registration Date</th>
              <th scope="col">Actions</th>
            </tr>
          </thead>
          <tbody>
            {customers.map((customer) => (
              <tr key={customer.id}>
                <td scope="row">{customer.id}</td>
                <td>
                  {editingCustomerId === customer.id ? (
                    <input
                      type="text"
                      name="firstName"
                      value={formData.firstName}
                      onChange={handleInputChange}
                    />
                  ) : (
                    customer.firstName
                  )}
                </td>
                <td>
                  {editingCustomerId === customer.id ? (
                    <input
                      type="text"
                      name="lastName"
                      value={formData.lastName}
                      onChange={handleInputChange}
                    />
                  ) : (
                    customer.lastName
                  )}
                </td>
                <td>
                  {editingCustomerId === customer.id ? (
                    <input
                      type="email"
                      name="email"
                      value={formData.email}
                      onChange={handleInputChange}
                    />
                  ) : (
                    customer.email
                  )}
                </td>
                <td>
                  {editingCustomerId === customer.id ? (
                    <select
                      name="region"
                      value={formData.region}
                      onChange={handleInputChange}
                    >
                      <option value="A">A</option>
                      <option value="B">B</option>
                      <option value="C">C</option>
                      <option value="D">D</option>
                    </select>
                  ) : (
                    customer.region
                  )}
                </td>
                <td>
          {new Date(customer.registrationDate).toLocaleDateString()} {/* Format date */}
        </td>
                <td>
                  {editingCustomerId === customer.id ? (
                    <>
                      <IconButtonWithToolTip
                        onClick={() => handleSaveCustomer(customer.id)}
                        icon={faCheck}
                        tooltipText="Save"
                        variant={"success"}
                        className="btn-sm me-2"
                      />
                      <IconButtonWithToolTip
                        onClick={handleCancelEdit}
                        icon={faTimes}
                        tooltipText={"Cancel"}
                        variant={"danger"}
                        className={"btn-sm"}
                      />
                    </>
                  ) : (
                    <>
                      <IconButtonWithToolTip
                        onClick={() => handleEditClick(customer)}
                        icon={faPencilAlt}
                        tooltipText={"Edit"}
                        variant={"warning"}
                        className={"btn-sm me-2"}
                      />
                      <IconButtonWithToolTip
                        onClick={() => handleDeleteClick(customer.id)}
                        icon={faTrash}
                        tooltipText={"Delete"}
                        variant={"danger"}
                        className={"btn-sm"}
                      />
                    </>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="d-flex justify-content-between">
        <Button
          onClick={() => setPage((prevPage) => Math.max(prevPage - 1, 0))}
          disabled={page === 0}
        >
          Previous Page
        </Button>
        <span>
          Page {page + 1} of {totalPages}
        </span>
        <Button
          onClick={() => setPage((prevPage) => Math.min(prevPage + 1, totalPages - 1))}
          disabled={page === totalPages - 1}
        >
          Next Page
        </Button>
      </div>

      <Modal show={showErrorModal} onHide={handleCloseErrorModal}>
        <Modal.Header closeButton>
          <Modal.Title>Error</Modal.Title>
        </Modal.Header>
        <Modal.Body>{error}</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseErrorModal}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>

      <ConfirmationModal
        show={showConfirmModal}
        onConfirm={handleConfirmDelete}
        onClose={handleCloseConfirmModal}
        message="Are you sure you want to delete this customer?"
      />
    </div>
  );
};

export default FilterCustomersWithSpecsPage;
