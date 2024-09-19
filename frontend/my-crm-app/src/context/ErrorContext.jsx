import { createContext, useContext, useState, useCallback } from "react";

const ErrorContext = createContext();

export const useError = () => useContext(ErrorContext);

export const ErrorProvider = ({ children }) => {
  const [error, setError] = useState(null);
  const [onCloseCallback, setOnCloseCallback] = useState(null); // Store onClose callback

  const triggerError = useCallback((message, onClose) => {
    setError(message);
    setOnCloseCallback(() => onClose); // Set the callback
  }, []);

  const clearError = () => {
    setError(null);
    if (onCloseCallback) onCloseCallback(); // Call the onClose callback if it exists
  };

  return (
    <ErrorContext.Provider value={{ error, triggerError, clearError }}>
      {children}
    </ErrorContext.Provider>
  );
};
