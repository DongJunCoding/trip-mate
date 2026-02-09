import { useState } from "react";
import axios from "axios";
import { setAccessToken } from "../../../util/tokenStorage";
import { useNavigate } from "react-router-dom";

function LoginPage() {
  const [userId, setUserId] = useState("");
  const [userPw, setUserPw] = useState("");

  const navigate = useNavigate();

  const loginApi = async () => {
    try {
      const res = await axios.post(
        "/api/login",
        {
          userId: userId,
          userPw: userPw,
        },
        { withCredentials: true },
      );

      console.log("res: ", res);

      // 이전 페이지로 이동 설정 필요
      if (res.status == 200) {
        setAccessToken(res.data.accessToken);
        navigate("/");
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div>
      <div>
        <h1>Login</h1>
        <label>아이디</label> &nbsp;
        <input
          type="text"
          placeholder="아이디"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
        />
        &nbsp;
        <label>비밀번호</label> &nbsp;
        <input
          type="password"
          placeholder="비밀번호"
          value={userPw}
          onChange={(e) => setUserPw(e.target.value)}
        />
      </div>
      <button onClick={loginApi}>로그인</button>
    </div>
  );
}

export default LoginPage;
