import { useState, useEffect } from "react";
import axios from "@/config/api/http";
import { useNavigate, useParams } from "react-router-dom";

function CreateSchedule() {
  const navigate = useNavigate();

  const { id } = useParams();

  const [values, setValues] = useState({
    startDate: "",
    endDate: "",
    teamName: "",
    destination: "",
  });

  // n일차 + 일정 구조
  const [days, setDays] = useState([]);

  /* =========================
     기본 여행 정보 변경
  ========================== */
  const onChange = (e) => {
    const { name, value } = e.target;
    setValues({ ...values, [name]: value });
  };

  useEffect(() => {
    console.log("useEffect id");
  });

  /* =========================
     여행 기간 기준 자동 n일차 생성
  ========================== */
  useEffect(() => {
    console.log("useEffect today");

    const getToday = () => {
      const date = new Date();
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      return `${year}-${month}-${day}`;
    };

    const today = getToday();

    if (!values.startDate || !values.endDate) return;
    if (values.startDate > values.endDate) {
      alert("날짜를 다시 선택해주세요.");
      values.startDate = "";
      values.endDate = "";
    }
    const start = new Date(values.startDate);
    const end = new Date(values.endDate);

    const tempDays = [];
    let current = new Date(start);
    let count = 1;

    while (current <= end) {
      tempDays.push({
        dayNum: count,
        scheduleDate: current.toISOString().split("T")[0],
        schedules: [],
      });

      current.setDate(current.getDate() + 1);
      count++;
    }

    setDays(tempDays);
  }, [values.startDate, values.endDate]);

  /* =========================
     일정 추가
  ========================== */
  const addSchedule = (dayIndex) => {
    const updated = [...days];

    updated[dayIndex].schedules.push({
      place: "",
      address: "",
      visitTime: "",
      memo: "",
    });

    setDays(updated);
  };

  /* =========================
     일정 삭제
  ========================== */
  const removeSchedule = (dayIndex, scheduleIndex) => {
    const updated = [...days];
    updated[dayIndex].schedules.splice(scheduleIndex, 1);
    setDays(updated);
  };

  /* =========================
     일차 삭제
  ========================== */
  const removeDay = (dayIndex) => {
    const updated = days.filter((_, i) => i !== dayIndex);

    // dayNum 재정렬
    // const reordered = updated.map((day, index) => ({
    //   ...day,
    //   dayNum: index + 1,
    // }));

    setDays(updated);
  };

  /* =========================
     일정 값 변경
  ========================== */
  const onChangeSchedule = (dayIndex, scheduleIndex, e) => {
    const { name, value } = e.target;

    const updated = [...days];
    updated[dayIndex].schedules[scheduleIndex][name] = value;

    setDays(updated);
  };

  /* =========================
     저장
  ========================== */
  const saveApi = async () => {
    try {
      const res = await axios.post("/api/v1/app/travel/saveSchedule", {
        ...values,
        days,
      });

      if (res.status === 200) {
        navigate("/teamList");
      }
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div className="container p-5">
      <h2 className="text-xl font-bold mb-4">여행 일정 생성</h2>

      {/* =========================
          기본 여행 정보
      ========================== */}

      <div className="mb-3">
        <label>팀명: </label>
        <input
          type="text"
          name="teamName"
          value={values.teamName}
          onChange={onChange}
          className="border ml-2"
        />
      </div>

      <div className="mb-3">
        <label>목적지: </label>
        <input
          type="text"
          name="destination"
          value={values.destination}
          onChange={onChange}
          className="border ml-2"
        />
      </div>

      <div className="mb-5">
        <label>여행 시작 날짜: </label>
        <input
          type="date"
          name="startDate"
          value={values.startDate}
          onChange={onChange}
          className="border ml-2"
        />

        <span className="mx-3">~</span>

        <label>여행 종료 날짜: </label>
        <input
          type="date"
          name="endDate"
          value={values.endDate}
          onChange={onChange}
          className="border ml-2"
        />
      </div>

      <hr />

      {/* =========================
          n일차 렌더링
      ========================== */}

      {days.map((day, dayIndex) => (
        <div key={dayIndex} className="border p-4 mt-6">
          <div className="flex justify-between items-center">
            <h3 className="font-bold">
              {day.dayNum}일차 ({day.scheduleDate})
            </h3>

            <button
              onClick={() => removeDay(dayIndex)}
              className="text-red-500"
            >
              일차 삭제
            </button>
          </div>

          {/* 일정 목록 */}
          {day.schedules.map((schedule, scheduleIndex) => (
            <div key={scheduleIndex} className="border p-3 mt-3">
              <div>
                <label>장소명: </label>
                <input
                  type="text"
                  name="place"
                  value={schedule.place}
                  onChange={(e) => onChangeSchedule(dayIndex, scheduleIndex, e)}
                  className="border ml-2"
                />
              </div>

              <div className="mt-2">
                <label>주소: </label>
                <input
                  type="text"
                  name="address"
                  value={schedule.address}
                  onChange={(e) => onChangeSchedule(dayIndex, scheduleIndex, e)}
                  className="border ml-2"
                />
              </div>

              <div className="mt-2">
                <label>방문 시간: </label>
                <input
                  type="time"
                  name="visitTime"
                  value={schedule.visitTime}
                  onChange={(e) => onChangeSchedule(dayIndex, scheduleIndex, e)}
                  className="border ml-2"
                />
              </div>

              <div className="mt-2">
                <label>메모: </label>
                <textarea
                  name="memo"
                  value={schedule.memo}
                  onChange={(e) => onChangeSchedule(dayIndex, scheduleIndex, e)}
                  className="border ml-2"
                />
              </div>

              <button
                onClick={() => removeSchedule(dayIndex, scheduleIndex)}
                className="text-red-500 mt-2"
              >
                일정 삭제
              </button>
            </div>
          ))}

          <button
            onClick={() => addSchedule(dayIndex)}
            className="mt-3 bg-gray-200 px-2 py-1"
          >
            + 일정 추가
          </button>
        </div>
      ))}

      <button onClick={saveApi} className="mt-8 bg-orange-300 px-4 py-2">
        저장
      </button>
    </div>
  );
}

export default CreateSchedule;
