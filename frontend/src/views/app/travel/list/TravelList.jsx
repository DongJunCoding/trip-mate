import axios from "@/config/api/http";
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

function TeamList() {
  const [values, setValues] = useState([]);

  const listApi = async () => {
    try {
      const res = await axios.post("/api/v1/app/travel/getTravelList", {});
      console.log(res);

      setValues(res.data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    listApi();
  }, []);

  return (
    <div className="container">
      <div className="flex flex-col">
        <div className="flex flex-row mb-5">
          <Link to="/schedule/create">일정추가</Link>
        </div>
        {values.map((item) => (
          <div key={item.id}>
            <div className="flex flex-row">
              <label htmlFor="teamName">팀명: </label>
              <input value={item.teamName} id="teamName" readOnly />
            </div>

            <div className="flex flex-row">
              <label htmlFor="destination">여행지: </label>
              <input value={item.destination} readOnly={true} />
            </div>

            <div className="flex flex-row">
              <label htmlFor="startDate">여행 시작: </label>
              <input value={item.startDate} readOnly={true} />
            </div>

            <div className="flex flex-row">
              <label htmlFor="endDate">여행 끝: </label>
              <input value={item.endDate} readOnly={true} />
            </div>

            <Link to={`/schedule/${item.id}`}>
              <button className="mt-8 bg-orange-300 px-4 py-2">보기</button>
            </Link>
            <hr />
          </div>
        ))}
      </div>
    </div>
  );
}

export default TeamList;
