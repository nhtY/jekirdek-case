import { Button, Modal } from "react-bootstrap";
import PropTypes from 'prop-types';

const ConfirmationModal = ({ show, onClose, onConfirm, message }) => (
    <Modal show={show} onHide={onClose} centered>
        <Modal.Header closeButton>
            <Modal.Title>Confirm Action</Modal.Title>
        </Modal.Header>
        <Modal.Body>{message}</Modal.Body>
        <Modal.Footer>
            <Button variant="secondary" onClick={onClose}>
                Cancel
            </Button>
            <Button variant="danger" onClick={onConfirm}>
                Confirm
            </Button>
        </Modal.Footer>
    </Modal>
);

ConfirmationModal.propTypes = {
    show: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    onConfirm: PropTypes.func.isRequired,
    message: PropTypes.string.isRequired,
};

export default ConfirmationModal;