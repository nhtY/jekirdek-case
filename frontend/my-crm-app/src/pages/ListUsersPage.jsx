
const ListUsersPage = () => {
    const handleDelete = (id) => {
        console.log(`User with id ${id} deleted`);
    };

    const handleUpdate = (id) => {
        console.log(`User with id ${id} updated`);
    };

    return (
        <div>
            <h2>List Users</h2>
            <table className="table">
                <thead>
                    <tr>
                        <th>Actions</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Username</th>
                        <th>Email</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <button onClick={() => handleDelete(1)}>Delete</button>
                            <button onClick={() => handleUpdate(1)}>Update</button>
                        </td>
                        <td>Jane</td>
                        <td>Doe</td>
                        <td>janedoe</td>
                        <td>jane.doe@example.com</td>
                    </tr>
                </tbody>
            </table>
        </div>
    );
};

export default ListUsersPage;
