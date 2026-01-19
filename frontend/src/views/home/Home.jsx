import axios from "../../config/api/http";
import LoginPage from "../user/login/Login";

function Home() {
  const testApi = async () => {
    const res = await axios.get("/api/v1/app/test/user");
    console.log("res", res);
  };

  const testApi2 = async () => {
    const res = await axios.get("/api/v1/app/test/sys");
    console.log("res", res);
  };
  return (
    <div>
      <h1 className="text-3xl font-bold">홈화면입니다.</h1>
      <p className="text-gray-500">화면 언제만드냐 진짜</p>
      <button onClick={testApi}>권한테스트 user api</button> <br />
      <button onClick={testApi2}>권한테스트 sys api</button>
    </div>
  );
}

export default Home;
