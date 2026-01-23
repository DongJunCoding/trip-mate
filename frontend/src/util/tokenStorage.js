export const ACCESS_TOKEN = "accessToken";

export const getAccessToken = () => {
  return localStorage.getItem(ACCESS_TOKEN);
};

export const setAccessToken = (accessToken) => {
  localStorage.setItem(ACCESS_TOKEN, accessToken);
};

export const clearTokens = () => {
  localStorage.removeItem(ACCESS_TOKEN);
};
