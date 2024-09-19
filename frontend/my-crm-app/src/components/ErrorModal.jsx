import { useError } from '../context/ErrorContext';

// Here we do not pass the onClose callback to the ErrorModal component in the App.jsx component,
// but in case any callback given, we call it when the modal is closed.

// The onClose callback in "ErrorContext" is used under clearError function to call the callback if it exists.
const ErrorModal = ({ onClose }) => {
  const { error, clearError } = useError();

  const handleClose = () => {
    clearError(); // Clear the error state
    if (onClose) onClose(); // Call the passed onClose callback if provided
  };

  if (!error) return null; // Don't render the modal if there's no error

  return (
    <div className="modal show d-block" tabIndex="-1">
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header bg-danger-subtle text-danger">
            <h5 className="modal-title">Error</h5>
            <button type="button" className="btn-close" onClick={handleClose}></button>
          </div>
          <div className="modal-body">
            <p>{error}</p>
          </div>
          <div className="modal-footer">
            <button type="button" className="btn btn-primary" onClick={handleClose}>
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ErrorModal;
