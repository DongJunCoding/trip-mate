import { Outlet } from "react-router-dom";

function Main() {
  return (
    <main>
      <div className="h-100">
        <Outlet />
      </div>
    </main>
  );
}

export default Main;
