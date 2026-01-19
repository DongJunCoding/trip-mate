import axios from "axios";
import {
  clearTokens,
  getAccessToken,
  getRefreshToken,
  setTokens,
} from "../../util/tokenStorage";

// .env로 부터 받은 백엔드 URL 받아오기
const BACKEND_API_BASE_URL = import.meta.env.VITE_BACKEND_API_BASE_URL;

const instance = axios.create({
  baseURL: BACKEND_API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

instance.interceptors.request.use(
  (config) => {
    const accessToken = getAccessToken();

    console.log("interceptors.request.use config : ", config);
    console.log("interceptor accessToken: ", accessToken);

    if (accessToken) {
      config.headers = config.headers ?? {};
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

instance.interceptors.response.use(
  (response) => response,
  async (error) => {
    console.error("error: ", error);

    const originalRequest = error.config; // error를 발생시킨 '그 요청의 설정 객체'를 다시 꺼내옴
    const refreshToken = getRefreshToken();

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const res = await axios.post(
          `${BACKEND_API_BASE_URL}/api/v1/common/jwt/refresh`,
          { refreshToken: refreshToken },
        );

        const { accessToken, refreshToken: newRefresh } = res.data;
        setTokens(accessToken, newRefresh); // 토큰 갱신

        // 새 토큰으로 다시 요청
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;

        return instance(originalRequest);
      } catch (error) {
        console.error("error: ", error);

        clearTokens();
        window.location.href = "/login";
        return Promise.reject(error);
      }
    }

    return Promise.reject(error);
  },
);

export default instance;
