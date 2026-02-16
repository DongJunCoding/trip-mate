import { Link } from "react-router-dom";
import axios from "../../config/api/http";

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
      <Link to="/login">로그인 화면 이동</Link> <br />
      <Link to="/travelList">나의 여행 리스트 화면 이동</Link> <br />
      <button onClick={testApi}>권한테스트 user api</button> <br />
      <button onClick={testApi2}>권한테스트 sys api</button> <br />
    </div>
  );
}

export default Home;
