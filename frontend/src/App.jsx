import { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import SignupPage from "./views/user/signup/Signup";
import LoginPage from "./views/user/login/Login";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/aaa" element={<LoginPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
