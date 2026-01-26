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

  const onChange = (e) => {
    const { name, value } = e.target;

    setUser({
      ...user,
      [name]: value,
    });
  };

  return (
    <div>
      <h1>회원가입 페이지</h1>
      <label>아이디</label> &nbsp;
      <input
        name="userId"
        type="text"
        placeholder="아이디"
        value={user.userId}
        onChange={onChange}
      />
      &nbsp; <br />
      <label>비밀번호</label> &nbsp;
      <input
        name="userPw"
        type="text"
        placeholder="비밀번호"
        value={user.userPw}
        onChange={onChange}
      />
      &nbsp; <br />
      <label>이메일</label> &nbsp;
      <input
        name="userEmail"
        type="text"
        placeholder="이메일"
        value={user.userEmail}
        onChange={onChange}
      />
      &nbsp; <br />
      <input type="hidden" value={"USER"} />
      <label>닉네임</label> &nbsp;
      <input
        name="nickname"
        type="text"
        placeholder="닉네임"
        value={user.nickname}
        onChange={onChange}
      />
      &nbsp; <br />
    </div>
  );
}

export default SignupPage;
