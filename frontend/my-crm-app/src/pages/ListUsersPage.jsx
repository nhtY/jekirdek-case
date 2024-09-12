import { useState } from 'react';
import IconButtonWithToolTip from '../components/IconButtonWithToolTip';
import { faPencilAlt, faTrash, faTimes, faCheck } from '@fortawesome/free-solid-svg-icons';
import ConfirmationModal from '../components/ConfirmationModal';

// A utility function for basic validation
const validateEmail = (email) => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
};

const validateName = (name) => {
  return name.length >= 3 && /^[A-Za-z]+$/.test(name);
};

const ListUsersPage = () => {
  // Initial list of users
  const initialUsers = [
    { id: 1, firstName: 'Jane', lastName: 'Doe', username: 'janedoe', email: 'jane.doe@example.com' },
    { id: 2, firstName: 'John', lastName: 'Smith', username: 'johnsmith', email: 'john.smith@example.com' },
  ];

  const [users, setUsers] = useState(initialUsers);
  const [showConfirm, setShowConfirm] = useState(false);
  const [deleteId, setDeleteId] = useState(null);
  const [editingId, setEditingId] = useState(null);
  const [errors, setErrors] = useState({});
  const [editedData, setEditedData] = useState({
    firstName: '',
    lastName: '',
    username: '',
    email: ''
  });

  // Handle Delete Operation
  const handleDelete = (id) => {
    setDeleteId(id);
    setShowConfirm(true);
  };

  const confirmDelete = () => {
    setUsers(users.filter(user => user.id !== deleteId));
    setShowConfirm(false);
    setDeleteId(null);
  };

  // Handle Update Operation
  const handleUpdate = (user) => {
    setEditingId(user.id);
    setEditedData({
      firstName: user.firstName,
      lastName: user.lastName,
      username: user.username,
      email: user.email,
    });
  };

  const cancelUpdate = () => {
    setEditingId(null);
    setErrors({});
    setEditedData({
      firstName: '',
      lastName: '',
      username: '',
      email: ''
    });
  };

  const validateForm = () => {
    const newErrors = {};
    if (!validateName(editedData.firstName)) {
      newErrors.firstName = 'First name must be at least 3 letters and contain only letters.';
    }
    if (!validateName(editedData.lastName)) {
      newErrors.lastName = 'Last name must be at least 3 letters and contain only letters.';
    }
    if (!validateEmail(editedData.email)) {
      newErrors.email = 'Please enter a valid email address.';
    }
    if (editedData.username.length < 3) {
      newErrors.username = 'Username must be at least 3 characters long.';
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const confirmUpdate = () => {
    if (validateForm()) {
      setUsers(users.map((user) =>
        user.id === editingId
          ? { ...user, ...editedData }
          : user
      ));
      setEditingId(null);
    }
  };

  return (
    <div>
      <h2>List Users</h2>
      <div className="table-responsive">
        <table className="table table-striped table-bordered table-hover">
          <thead className="table-dark">
            <tr>
              <th scope="col">ID</th>
              <th scope="col">First Name</th>
              <th scope="col">Last Name</th>
              <th scope="col">Username</th>
              <th scope="col">Email</th>
              <th scope="col">Actions</th>
            </tr>
          </thead>
          <tbody className="table-group-divider">
            {users.map((user) => (
              <tr key={user.id} className={editingId === user.id ? 'table-warning' : ''}>
                <td scope="row">{user.id}</td>
                {editingId === user.id ? (
                  <>
                    <td>
                      <input
                        type="text"
                        value={editedData.firstName}
                        onChange={(e) => setEditedData({ ...editedData, firstName: e.target.value })}
                        className={errors.firstName ? 'is-invalid' : ''}
                      />
                      {errors.firstName && <div className="invalid-feedback">{errors.firstName}</div>}
                    </td>
                    <td>
                      <input
                        type="text"
                        value={editedData.lastName}
                        onChange={(e) => setEditedData({ ...editedData, lastName: e.target.value })}
                        className={errors.lastName ? 'is-invalid' : ''}
                      />
                      {errors.lastName && <div className="invalid-feedback">{errors.lastName}</div>}
                    </td>
                    <td>
                      <input
                        type="text"
                        value={editedData.username}
                        onChange={(e) => setEditedData({ ...editedData, username: e.target.value })}
                        className={errors.username ? 'is-invalid' : ''}
                      />
                      {errors.username && <div className="invalid-feedback">{errors.username}</div>}
                    </td>
                    <td>
                      <input
                        type="email"
                        value={editedData.email}
                        onChange={(e) => setEditedData({ ...editedData, email: e.target.value })}
                        className={errors.email ? 'is-invalid' : ''}
                      />
                      {errors.email && <div className="invalid-feedback">{errors.email}</div>}
                    </td>
                    <td>
                      <IconButtonWithToolTip
                        variant="secondary"
                        icon={faTimes}
                        tooltipText="Cancel Update"
                        onClick={cancelUpdate}
                        className="me-2"
                      />
                      <IconButtonWithToolTip
                        variant="primary"
                        icon={faCheck}
                        tooltipText="Confirm Update"
                        onClick={confirmUpdate}
                      />
                    </td>
                  </>
                ) : (
                  <>
                    <td>{user.firstName}</td>
                    <td>{user.lastName}</td>
                    <td>{user.username}</td>
                    <td>{user.email}</td>
                    <td>
                      <IconButtonWithToolTip
                        variant="warning"
                        icon={faPencilAlt}
                        tooltipText="Edit User"
                        onClick={() => handleUpdate(user)}
                        className="me-2"
                      />
                      <IconButtonWithToolTip
                        variant="danger"
                        icon={faTrash}
                        tooltipText="Delete User"
                        onClick={() => handleDelete(user.id)}
                      />
                    </td>
                  </>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Confirmation Modal */}
      <ConfirmationModal
        show={showConfirm}
        onClose={() => setShowConfirm(false)}
        onConfirm={confirmDelete}
        message="Are you sure you want to delete this user?"
      />
    </div>
  );
};

export default ListUsersPage;
