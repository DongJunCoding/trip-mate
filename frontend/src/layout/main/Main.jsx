import { Outlet } from "react-router-dom";

function Main() {
  return (
    <main>
      <div className="">
        <Outlet />
      </div>
    </main>
  );
}

export default Main;
