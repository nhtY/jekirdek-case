import { Button } from "react-bootstrap";

const ListCustomersPage = () => {
  const handleDelete = (id) => {
    console.log(`Customer with id ${id} deleted`);
  };

  const handleUpdate = (id) => {
    console.log(`Customer with id ${id} updated`);
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
            <tr>
              <td scope="row">1</td>
              <td>John</td>
              <td>Doe</td>
              <td>john.doe@example.com</td>
              <td>North</td>
              <td>
                <Button
                  variant="warning"
                  onClick={() => handleUpdate(1)}
                  className="me-2"
                >
                  Update
                </Button>
                <Button variant="danger" onClick={() => handleDelete(1)}>
                  Delete
                </Button>
              </td>
            </tr>
            {/* Add more rows as needed */}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ListCustomersPage;
