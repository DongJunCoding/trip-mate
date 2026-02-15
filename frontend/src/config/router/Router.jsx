import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomePage from "../../views/home/Home";
import LoginPage from "../../views/user/login/Login";
import SignupPage from "../../views/user/signup/Signup";
import PageNotFound from "../../views/error/404Page";
import Layout from "../../layout/index";
import TeamList from "../../views/app/travel/list/TravelList";
import Schedule from "../../views/app/travel/schedule/TravelSchedule";

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signUp" element={<SignupPage />} />
          <Route path="/teamList" element={<TeamList />} />
          <Route path="/schedule/create" element={<Schedule />} />
          <Route path="/schedule/:id" element={<Schedule />} />
          <Route path="/*" element={<PageNotFound />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
