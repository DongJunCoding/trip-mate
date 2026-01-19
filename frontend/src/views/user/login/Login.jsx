import { useState } from "react";
import axios from "axios";
import { setTokens } from "../../../util/tokenStorage";

function LoginPage() {
  const [userId, setUserId] = useState("");
  const [userPw, setUserPw] = useState("");

  const loginApi = async () => {
    try {
      const res = await axios.post("/api/login", {
        userId: userId,
        userPw: userPw,
      });

      console.log("res: ", res);
      if (res.status == 200) {
        setTokens(res.data.accessToken, res.data.refreshToken);
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
