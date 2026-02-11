import Header from "./header/Header";
import Main from "./main/Main";
import Footer from "./footer/Footer";

function Layout() {
  return (
    <div className="min-h-screen flex flex-col">
      <Header />
      <Main />
      <Footer />
    </div>
  );
}

export default Layout;
