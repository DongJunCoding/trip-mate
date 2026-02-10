import { useState } from "react";
import axios from "../../../../config/api/http";
import { useNavigate } from "react-router-dom";

function CreateSchedule() {
  const [values, setValues] = useState({
    startDate: "",
    endDate: "",
    teamName: "",
    destination: "",
  });

  const onChange = (e) => {
    const { name, value } = e.target;

    setValues({ ...values, [name]: value });
  };

  const saveApi = async () => {
    try {
      const res = await axios.post(
        "/api/v1/app/travel/saveSchedule",
        {
          ...values,
        },
        {},
      );
      console.log(res);

      if (res.status == 200) {
        navigate("/teamList");
      }
    } catch (e) {
      console.error(e);
    }
  };
  return (
    <div className="container">
      <div className="flex flex-col">
        <div className="flex flex-row">
          <label htmlFor="startDate">여행 시작 날짜: </label>
          <input
            type="date"
            name="startDate"
            id="startDate"
            value={values.startDate}
            onChange={onChange}
            className="ml-5"
          />

          <span className="ml-5 mr-5">~</span>

          <label htmlFor="endDate">여행 종료 날짜: </label>
          <input
            type="date"
            name="endDate"
            id="endDate"
            value={values.endDate}
            onChange={onChange}
            className="ml-5"
          />
        </div>

        <div className="flex flex-row"></div>

        <div className="flex flex-row">
          <label htmlFor="teamName">팀명: </label>
          <input
            type="text"
            name="teamName"
            id="teamName"
            value={values.teamName}
            onChange={onChange}
            className="ml-5"
          />
        </div>

        <div className="flex flex-row">
          <label htmlFor="destination">목적지: </label>
          <input
            type="text"
            name="destination"
            id="destination"
            value={values.destination}
            onChange={onChange}
            className="ml-5"
          />
        </div>
        <div className="flex flex-row mt-5">
          <button
            type="button"
            className="border bg-orange-100"
            onClick={saveApi}
          >
            저장버튼
          </button>
        </div>
      </div>
    </div>
  );
}

export default CreateSchedule;
