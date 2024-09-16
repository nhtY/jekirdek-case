// src/components/NotificationModal.jsx

import { Modal, Button } from 'react-bootstrap';
import PropTypes from 'prop-types';

const NotificationModal = ({ show, onClose, message, type }) => {
    const getModalTitle = () => {
        switch (type) {
            case 'error':
                return 'Error';
            case 'success':
                return 'Success';
            case 'info':
                return 'Info';
            case 'warning':
                return 'Warning';
            default:
                return 'Notification';
        }
    };

    const getModalVariant = () => {
        switch (type) {
            case 'error':
                return 'danger';
            case 'success':
                return 'success';
            case 'info':
                return 'info';
            case 'warning':
                return 'warning';
            default:
                return 'secondary';
        }
    };

    return (
        <Modal show={show} onHide={onClose} centered>
            <Modal.Header closeButton className={`bg-${getModalVariant()}`}>
                <Modal.Title>{getModalTitle()}</Modal.Title>
            </Modal.Header>
            <Modal.Body>{message}</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onClose}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

NotificationModal.propTypes = {
    show: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    message: PropTypes.string.isRequired,
    type: PropTypes.oneOf(['error', 'success', 'info', 'warning']).isRequired,
};

export default NotificationModal;
