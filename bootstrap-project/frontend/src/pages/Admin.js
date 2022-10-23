import React, { Component } from "react";
import Table from "react-bootstrap/Table";
import Alert from "react-bootstrap/Alert";

class Admin extends Component {
  constructor(props) {
    super(props);
    this.state = { rentedCars: [], profit: null };
  }

  async fetchRentedCars() {
    try {
      const response = await fetch("http://localhost:8080/api/admin");
      const res = await response.json();
      if (response.ok) {
        this.setState({ rentedCars: res });
        return;
      }
      const responseMessage = await response.text();
      throw new Error(responseMessage);
    } catch (e) {
      alert(`Something went wrong ${e.message}`);
    }
  }

  async fetchProfit() {
    try {
      const response = await fetch("http://localhost:8080/api/profit");
      if (response.ok) {
        const res = await response.json();
        this.setState({ profit: res });
        return;
      }
      const responseMessage = await response.text();
      throw new Error(responseMessage);
    } catch (e) {
      alert(`Something went wrong ${e.message}`);
    }
  }

  componentDidMount() {
    this.fetchRentedCars();
    this.fetchProfit();
  }

  render() {
    return (
      <>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Car</th>
              <th>Name driver</th>
              <th>From to</th>
              <th>Revenue</th>
            </tr>
          </thead>
          <tbody>
            {this.state.rentedCars.map((rented, index) => (
              <tr key={index}>
                <td>{rented.car.name}</td>

                <td>{rented.nameOFDriver}</td>
                <td>{rented.fromTo}</td>
                <td>{rented.revenue} SEK</td>
              </tr>
            ))}
          </tbody>
        </Table>
        {this.state.profit && (
          <Alert style={{ marginTop: 10 }} variant="success">
            Profit: {this.state.profit} SEK
          </Alert>
        )}
      </>
    );
  }
}

export default Admin;
