import axios from "axios";

// .env로 부터 받은 백엔드 URL 받아오기
const BACKEND_API_BASE_URL = import.meta.env.VITE_BACKEND_API_BASE_URL;
const accessToken = localStorage.getItem("accessToken");

const http = axios.create({
  baseURL: BACKEND_API_BASE_URL,
  headers: {
    Authorization: `Bearer ${accessToken}`,
    "Content-Type": "application/json",
  },
});

http.interceptors.request.use(
  (config) => {
    console.log("interceptors.request.use config : ", config);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

http.interceptors.response.use(
  (response) => {
    console.log("interceptors.response.use config : ", config);
    return response;
  },
  (error) => {
    return Promise.reject(error);
  }
);
