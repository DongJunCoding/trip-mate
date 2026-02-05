import { useState } from "react";
import { Link } from "react-router-dom";

function TeamList() {
  const [teamName, setTeamName] = useState("");
  const [destination, setDestination] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  return (
    <div className="container">
      <div className="flex flex-col">
        <div className="flex flex-row">
          <label htmlFor="teamName">팀명: </label>
          <input value={teamName} id="teamName" readOnly />

          <div className="flex flex-row">
            <Link to="/createSchedule">일정추가</Link>
          </div>
        </div>
        <div className="flex flex-row">
          <label htmlFor="destination">여행지: </label>
          <input value={destination} readOnly={true} />
        </div>
        <div className="flex flex-row">
          <label htmlFor="startDate">여행 시작: </label>
          <input value={startDate} readOnly={true} />
        </div>
        <div className="flex flex-row">
          <label htmlFor="endDate">여행 끝: </label>
          <input value={endDate} readOnly={true} />
        </div>
      </div>
    </div>
  );
}

export default TeamList;
