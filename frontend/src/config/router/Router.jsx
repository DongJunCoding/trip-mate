import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomePage from "../../views/home/index";
import LoginPage from "../../views/user/login";
import SignupPage from "../../views/user/signup";
import PageNotFound from "../../views/error/404Page";
import Layout from "../../layout/index";
import TeamList from "../../views/app/travel/list";
import CreateSchedule from "../../views/app/travel/detail";

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signUp" element={<SignupPage />} />
          <Route path="/teamList" element={<TeamList />} />
          <Route path="/createSchedule" element={<CreateSchedule />} />
          <Route path="/*" element={<PageNotFound />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
