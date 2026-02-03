import { useState } from "react";

function TeamList() {
  const [teamName, setTeamName] = useState("");
  const [destination, setDestination] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  return (
    <div>
      <label htmlFor="teamName"></label>
      <input value={teamName} id="teamName" readOnly />

      <label htmlFor="destination">여행지: </label>
      <input value={destination} readOnly={true} />

      <label htmlFor="startDate">여행 시작</label>
      <input value={startDate} readOnly={true} />

      <label htmlFor="endDate">여행 끝</label>
      <input value={endDate} readOnly={true} />
    </div>
  );
}

export default TeamList;
