import axios from "axios";

axios.defaults.baseURL = "http://localhost:8080/api/v1"; // backend URL
// Set up Axios interceptors for custom headers
axios.interceptors.request.use(
  (config) => {
    const browser = getBrowser();

    config.headers["device-os"] = browser !== 'Unknown'? 'Browser' : 'No-info';

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Utility function to detect browser
const getBrowser = () => {
  const userAgent = window.navigator.userAgent;
  if (userAgent.includes("Chrome")) return "Chrome";
  if (userAgent.includes("Firefox")) return "Firefox";
  if (userAgent.includes("Safari") && !userAgent.includes("Chrome"))
    return "Safari";
  if (userAgent.includes("Edge")) return "Edge";
  return "Unknown";
};

export default axios;
