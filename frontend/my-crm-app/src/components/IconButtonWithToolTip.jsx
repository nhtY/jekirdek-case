import { Button, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

// eslint-disable-next-line react/prop-types
const IconButtonWithToolTip = ({ icon, tooltipText, onClick, variant, className }) => {
  return (
    <OverlayTrigger
      placement="top"
      overlay={<Tooltip id={`tooltip-${tooltipText}`}>{tooltipText}</Tooltip>}
    >
      <Button variant={variant} onClick={onClick} className={className}>
        <FontAwesomeIcon icon={icon} />
      </Button>
    </OverlayTrigger>
  );
};

export default IconButtonWithToolTip;
