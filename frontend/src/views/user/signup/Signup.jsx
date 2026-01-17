import axios from "axios";
import { useState } from "react";

// .env로 부터 받은 백엔드 URL 받아오기
const BACKEND_API_BASE_URL = import.meta.env.VITE_BACKEND_API_BASE_URL;

function SignupPage() {
  const [user, setUser] = useState({
    userId: "",
    userPw: "",
    userEmail: "",
    userRole: "",
    nickname: "",
  });

  const signUpApi = async () => {
    try {
      const res = await axios.post("/signup", user);

      console.log("res: ", res);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div>
      <h1>회원가입 페이지</h1>
      <label>아이디</label> &nbsp;
      <input
        type="text"
        placeholder="아이디"
        value={userId}
        onChange={(e) => setUserId(e.target.value)}
      />
      &nbsp; <br />
      <label>비밀번호</label> &nbsp;
      <input
        type="text"
        placeholder="비밀번호"
        value={userPw}
        onChange={(e) => setUserPw(e.target.value)}
      />
      &nbsp; <br />
      <label>이메일</label> &nbsp;
      <input
        type="text"
        placeholder="이메일"
        value={userEmail}
        onChange={(e) => setUserEmail(e.target.value)}
      />
      &nbsp; <br />
      <input type="hidden" value={"USER"} />
      <label>닉네임</label> &nbsp;
      <input
        type="text"
        placeholder="닉네임"
        value={nickname}
        onChange={(e) => setNickname(e.target.value)}
      />
      &nbsp; <br />
    </div>
  );
}

export default SignupPage;

// "userId": "test1",
// "userPw": "zx1203",
// "userEmail": "test@naver.com",
// "userRole": "ROLE_USER",
// "nickname": "테스트1"
