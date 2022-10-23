import React, { Component } from "react";
import { Link } from "react-router-dom";

export default class MainScreen extends Component {
  render() {
    return (
      <div>
        <nav>
          <ul>
            <li>
              <Link to="/rent">Rent car</Link>
            </li>
            <li>
              <Link to="/admin">Admin panel</Link>
            </li>
          </ul>
        </nav>
      </div>
    );
  }
}
