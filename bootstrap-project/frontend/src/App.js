import "./App.css";
import { Component } from "react";
import RentCar from "./pages/RentCar";
import MainScreen from "./pages/MainScreen";
import Admin from "./pages/Admin";
import { BrowserRouter, Routes, Switch, Route, Link } from "react-router-dom";
import "react-datepicker/dist/react-datepicker.css";

class App extends Component {
  render() {
    return (
      <BrowserRouter>
        <Routes>
          <Route path="/admin" element={<Admin />}></Route>
          <Route path="/rent" element={<RentCar />}></Route>
          <Route path="/" element={<MainScreen />}></Route>
        </Routes>
      </BrowserRouter>
    );
  }
}

export default App;
////<Route path="/admin" element={<Admin />}></Route>
