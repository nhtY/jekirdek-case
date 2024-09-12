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

const ListCustomersPage = () => {
  // Initial list of 10 customers
  const initialCustomers = [
    { id: 1, firstName: 'John', lastName: 'Doe', email: 'john.doe@example.com', region: 'North' },
    { id: 2, firstName: 'Jane', lastName: 'Smith', email: 'jane.smith@example.com', region: 'South' },
    { id: 3, firstName: 'Michael', lastName: 'Johnson', email: 'michael.johnson@example.com', region: 'East' },
    { id: 4, firstName: 'Emily', lastName: 'Davis', email: 'emily.davis@example.com', region: 'West' },
    { id: 5, firstName: 'Chris', lastName: 'Brown', email: 'chris.brown@example.com', region: 'North' },
    { id: 6, firstName: 'David', lastName: 'Wilson', email: 'david.wilson@example.com', region: 'South' },
    { id: 7, firstName: 'Sarah', lastName: 'Moore', email: 'sarah.moore@example.com', region: 'East' },
    { id: 8, firstName: 'James', lastName: 'Taylor', email: 'james.taylor@example.com', region: 'West' },
    { id: 9, firstName: 'Mary', lastName: 'Anderson', email: 'mary.anderson@example.com', region: 'North' },
    { id: 10, firstName: 'Robert', lastName: 'Thomas', email: 'robert.thomas@example.com', region: 'South' },
  ];

  const regions = ['North', 'South', 'East', 'West'];

  const [customers, setCustomers] = useState(initialCustomers);
  const [showConfirm, setShowConfirm] = useState(false);
  const [deleteId, setDeleteId] = useState(null);
  const [editingId, setEditingId] = useState(null);
  const [errors, setErrors] = useState({});
  const [editedData, setEditedData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    region: ''
  });

  // Handle Delete Operation
  const handleDelete = (id) => {
    setDeleteId(id);
    setShowConfirm(true);
  };

  const confirmDelete = () => {
    setCustomers(customers.filter(customer => customer.id !== deleteId));
    setShowConfirm(false);
    setDeleteId(null);
  };

  // Handle Update Operation
  const handleUpdate = (customer) => {
    setEditingId(customer.id);
    setEditedData({
      firstName: customer.firstName,
      lastName: customer.lastName,
      email: customer.email,
      region: customer.region,
    });
  };

  const cancelUpdate = () => {
    setEditingId(null);
    setErrors({});
    setEditedData({
      firstName: '',
      lastName: '',
      email: '',
      region: ''
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
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const confirmUpdate = () => {
    if (validateForm()) {
      setCustomers(customers.map((customer) =>
        customer.id === editingId
          ? { ...customer, ...editedData }
          : customer
      ));
      setEditingId(null);
    }
  };

  return (
    <div>
      <h2>List Customers</h2>
      <div className="table-responsive">
        <table className="table table-striped table-bordered table-hover">
          <thead className="table-dark">
            <tr>
              <th scope="col">ID</th>
              <th scope="col">First Name</th>
              <th scope="col">Last Name</th>
              <th scope="col">Email</th>
              <th scope="col">Region</th>
              <th scope="col">Actions</th>
            </tr>
          </thead>
          <tbody className="table-group-divider">
            {customers.map((customer) => (
              <tr key={customer.id} className={editingId === customer.id ? 'table-warning' : ''}>
                <td scope="row">{customer.id}</td>
                {editingId === customer.id ? (
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
                        type="email"
                        value={editedData.email}
                        onChange={(e) => setEditedData({ ...editedData, email: e.target.value })}
                        className={errors.email ? 'is-invalid' : ''}
                      />
                      {errors.email && <div className="invalid-feedback">{errors.email}</div>}
                    </td>
                    <td>
                      <select
                        value={editedData.region}
                        onChange={(e) => setEditedData({ ...editedData, region: e.target.value })}
                      >
                        {regions.map((region) => (
                          <option key={region} value={region}>
                            {region}
                          </option>
                        ))}
                      </select>
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
                    <td>{customer.firstName}</td>
                    <td>{customer.lastName}</td>
                    <td>{customer.email}</td>
                    <td>{customer.region}</td>
                    <td>
                      <IconButtonWithToolTip
                        variant="warning"
                        icon={faPencilAlt}
                        tooltipText="Edit Customer"
                        onClick={() => handleUpdate(customer)}
                        className="me-2"
                      />
                      <IconButtonWithToolTip
                        variant="danger"
                        icon={faTrash}
                        tooltipText="Delete Customer"
                        onClick={() => handleDelete(customer.id)}
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
        message="Are you sure you want to delete this customer?"
      />
    </div>
  );
};

export default ListCustomersPage;
