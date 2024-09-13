import { useEffect, useState } from "react";
import axios from "../axios/axios";
import { Modal, Button } from "react-bootstrap";
import IconButtonWithToolTip from "../components/IconButtonWithToolTip"; // Assume you have a component for tooltips
import {
  faPencilAlt,
  faTrash,
  faCheck,
  faTimes,
} from "@fortawesome/free-solid-svg-icons";
import ConfirmationModal from "../components/ConfirmationModal"; // Assume you have a confirmation modal component

const ListUsersPage = () => {
  const [users, setUsers] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [sort, setSort] = useState("");
  const [error, setError] = useState(null);
  const [editingUserId, setEditingUserId] = useState(null);
  const [deleteUserId, setDeleteUserId] = useState(null);
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
  });
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);

  const handleCloseErrorModal = () => setShowErrorModal(false);
  const handleCloseConfirmModal = () => setShowConfirmModal(false);

  const fetchUsers = async (pageNumber, size, sort) => {
    try {
      const response = await axios.get("/users/page", {
        params: {
          page: pageNumber,
          size: size,
          sort: sort ? sort : [],
        },
      });
      setUsers(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error("FETCH ERROR: ", error);
      setError("Failed to load users.");
      setShowErrorModal(true);
    }
  };

  const handleEditClick = (user) => {
    setEditingUserId(user.id);
    setFormData({
      firstName: user.firstName,
      lastName: user.lastName,
    });
  };

  const handleCancelEdit = () => {
    setEditingUserId(null);
    setFormData({
      firstName: "",
      lastName: "",
    });
  };

  const handleSaveUser = async (id) => {
    try {
      const response = await axios.put(`/users/${id}`, formData);
      const updatedUsers = users.map((user) =>
        user.id === id ? { ...user, ...response.data } : user
      );
      setUsers(updatedUsers);
      setEditingUserId(null);
    } catch (error) {
      setError("Failed to update user.");
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

  const handleDeleteClick = (id) => {
    setDeleteUserId(id);
    setShowConfirmModal(true); // Show confirmation modal
  };

  const handleConfirmDelete = async () => {
    try {
      await axios.delete(`/users/${deleteUserId}`);
      setUsers(users.filter((user) => user.id !== deleteUserId));
      setDeleteUserId(null);
      setShowConfirmModal(false);
    } catch (error) {
      setError("Failed to delete user.");
      setShowErrorModal(true);
    }
  };

  useEffect(() => {
    fetchUsers(page, pageSize, sort);
  }, [page, pageSize, sort]);

  return (
    <div className="container-fluid">
      <h3>User List</h3>

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
          </select>
        </div>
      </div>

      <div className="table-responsive">
        <table className="table table-sm table-striped table-bordered table-hover">
        <thead className="table-dark">
            <tr>
              <th scope="col">ID</th>
              <th scope="col">First Name</th>
              <th scope="col">Last Name</th>
              <th scope="col">Username</th>
              <th scope="col">Email</th>
              <th scope="col">Roles</th>
              <th scope="col">Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td scope="row">{user.id}</td>
                <td>
                  {editingUserId === user.id ? (
                    <input
                      type="text"
                      name="firstName"
                      value={formData.firstName}
                      onChange={handleInputChange}
                    />
                  ) : (
                    user.firstName
                  )}
                </td>
                <td>
                  {editingUserId === user.id ? (
                    <input
                      type="text"
                      name="lastName"
                      value={formData.lastName}
                      onChange={handleInputChange}
                    />
                  ) : (
                    user.lastName
                  )}
                </td>
                <td>{user.username}</td>
                <td>{user.email}</td>
                <td>{user.roles}</td>
                <td>
                  {editingUserId === user.id ? (
                    <>
                      <IconButtonWithToolTip
                        icon={faCheck}
                        variant="secondary"
                        tooltipText="confirm"
                        onClick={() => handleSaveUser(user.id)}
                        className={"me-2"}
                      />
                      <IconButtonWithToolTip
                        icon={faTimes}
                        variant="danger"
                        tooltipText="Cancel"
                        onClick={handleCancelEdit}
                      />
                    </>
                  ) : (
                    <>
                      <IconButtonWithToolTip
                        icon={faPencilAlt}
                        variant="warning"
                        tooltipText="Edit"
                        onClick={() => handleEditClick(user)}
                        className={"me-2"}
                      />
                      <IconButtonWithToolTip
                        icon={faTrash}
                        variant="danger"
                        tooltipText="Delete"
                        onClick={() => handleDeleteClick(user.id)}
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
          className="btn btn-secondary"
          onClick={() => setPage((prevPage) => Math.max(prevPage - 1, 0))}
          disabled={page === 0}
        >
          Previous
        </Button>
        <span>
          Page {page + 1} of {totalPages}
        </span>
        <Button
          className="btn btn-secondary"
          onClick={() =>
            setPage((prevPage) => Math.min(prevPage + 1, totalPages - 1))
          }
          disabled={page >= totalPages - 1}
        >
          Next
        </Button>
      </div>

      {/* Error Modal */}
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

      {/* Confirmation Modal */}
      <ConfirmationModal
        show={showConfirmModal}
        onHide={handleCloseConfirmModal}
        onConfirm={handleConfirmDelete}
        message="Are you sure you want to delete this user?"
      />
    </div>
  );
};

export default ListUsersPage;
