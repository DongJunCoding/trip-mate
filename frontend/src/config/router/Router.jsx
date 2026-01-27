import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomePage from "../../views/home/Home";
import LoginPage from "../../views/user/login/Login";
import SignupPage from "../../views/user/signup/Signup";

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signUp" element={<SignupPage />} />
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
