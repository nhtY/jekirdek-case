
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
            <table className="table">
                <thead>
                    <tr>
                        <th>Actions</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>Region</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <button onClick={() => handleDelete(1)}>Delete</button>
                            <button onClick={() => handleUpdate(1)}>Update</button>
                        </td>
                        <td>John</td>
                        <td>Doe</td>
                        <td>john.doe@example.com</td>
                        <td>North</td>
                    </tr>
                </tbody>
            </table>
        </div>
    );
};

export default ListCustomersPage;
