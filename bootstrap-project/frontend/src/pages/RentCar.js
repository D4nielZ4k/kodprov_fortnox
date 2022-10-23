import React, { Component } from "react";
import Alert from "react-bootstrap/Alert";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import moment from "moment";
import containsNumber from "../helpers/containsNumber";
import getCurrentDate from "../helpers/getCurrentDate";

const REQUIRED_FIELD_MESSAGE = "Field is required";

const INITIAL_DATE = {
  carId: "",
  start: "",
  end: "",
  driverName: "",
  ageDriver: "",
};

class RentCar extends Component {
  constructor(props) {
    super(props);

    this.state = {
      options: [],
      summaryCost: null,
      form: INITIAL_DATE,
      errors: {},
      currentDate: getCurrentDate(),
    };
    this.handleChange = this.handleChange.bind(this);
  }

  async componentDidMount() {
    try {
      const response = await fetch("http://localhost:8080/api/cars");

      if (response.ok) {
        const res = await response.json();
        this.setState({ options: res });
        return;
      }
      const responseMessage = await response.text();
      throw new Error(responseMessage);
    } catch (e) {
      alert(`Something went wrong ${e.message}`);
    }
  }

  validateForm() {
    let errors = {};
    let isValid = true;

    if (this.state.form.carId === "") {
      errors.carId = REQUIRED_FIELD_MESSAGE;
      isValid = false;
    }

    if (this.state.form.start === "") {
      errors.start = REQUIRED_FIELD_MESSAGE;
      isValid = false;
    } else if (
      moment(this.state.form.start).isBefore(moment(this.state.currentDate))
    ) {
      errors.start = "Start date cannot be a date in the past";
      isValid = false;
    }

    if (this.state.form.end === "") {
      errors.end = REQUIRED_FIELD_MESSAGE;
      isValid = false;
    } else if (
      moment(this.state.form.end).isBefore(moment(this.state.form.start))
    ) {
      errors.end = "End date cannot be before start date";
      isValid = false;
    } else if (
      moment(this.state.form.end).isSame(moment(this.state.form.start))
    ) {
      errors.end = "End date cannot be a pick-up date";
      isValid = false;
    }

    if (this.state.form.end === "") {
      errors.end = REQUIRED_FIELD_MESSAGE;
      isValid = false;
    }

    if (this.state.form.driverName === "") {
      errors.driverName = REQUIRED_FIELD_MESSAGE;
      isValid = false;
    } else if (containsNumber(this.state.form.driverName)) {
      errors.driverName = "Driver name cannot contain numbers";
      isValid = false;
    }

    if (this.state.form.ageDriver === "") {
      errors.ageDriver = REQUIRED_FIELD_MESSAGE;
      isValid = false;
    } else if (parseInt(this.state.form.ageDriver) < 18) {
      errors.ageDriver = "Driver age must be 18 or older";
      isValid = false;
    }

    this.setState({ errors: errors });

    return isValid;
  }

  handleSubmit = (event) => {
    event.preventDefault();

    const isValid = this.validateForm();

    if (!isValid) return;

    this.registerRent(
      parseInt(this.state.form.carId),
      this.state.form.start,
      this.state.form.end,
      this.state.form.driverName,
      parseInt(this.state.form.ageDriver)
    );
  };

  async registerRent(carId, start, end, driverName, ageDriver) {
    try {
      const response = await fetch("http://localhost:8080/api", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          carId: carId,
          start: start,
          end: end,
          driverName: driverName,
          ageDriver: ageDriver,
        }),
      });

      if (response.ok) {
        const res = await response.json();
        this.setState({ form: INITIAL_DATE, summaryCost: res });
        return;
      }
      const responseMessage = await response.text();
      throw new Error(responseMessage);
    } catch (e) {
      this.setState({ summaryCost: null });
      alert(`Something went wrong ${e.message}`);
    }
  }

  handleChange(e) {
    let { value, name } = e.target;

    this.setState({
      form: { ...this.state.form, [name]: value },
    });
  }

  render() {
    return (
      <>
        <Form onSubmit={this.handleSubmit}>
          <h1 className="RegisterHeader">Rent Car</h1>

          <Form.Group controlId="start" size="lg">
            <Form.Label>Car</Form.Label>
            <Form.Select
              isInvalid={!!this.state.errors.carId}
              value={this.state.form.carId}
              name="carId"
              onChange={this.handleChange}
            >
              <option value="">Select a car</option>
              {this.state.options.map((option) => (
                <option key={option.id} value={option.id}>
                  {option.name}
                </option>
              ))}
            </Form.Select>
            {this.state.errors.carId && (
              <Form.Control.Feedback type="invalid">
                {this.state.errors.carId}{" "}
              </Form.Control.Feedback>
            )}
          </Form.Group>

          <Form.Group controlId="start" size="lg">
            <Form.Label>Start date</Form.Label>
            <Form.Control
              isInvalid={!!this.state.errors.start}
              value={this.state.form.start}
              type="date"
              name="start"
              format="yyyy-MM-dd"
              onChange={this.handleChange}
            />
            {this.state.errors.start && (
              <Form.Control.Feedback type="invalid">
                {this.state.errors.start}
              </Form.Control.Feedback>
            )}
          </Form.Group>

          <Form.Group controlId="end" size="lg">
            <Form.Label>End date</Form.Label>
            <Form.Control
              isInvalid={!!this.state.errors.end}
              value={this.state.form.end}
              type="date"
              name="end"
              format="yyyy-MM-dd"
              onChange={this.handleChange}
            />
            {this.state.errors.end && (
              <Form.Control.Feedback type="invalid">
                {this.state.errors.end}
              </Form.Control.Feedback>
            )}
          </Form.Group>

          <Form.Group controlId="driverName" size="lg">
            <Form.Label>Driver name</Form.Label>
            <Form.Control
              isInvalid={!!this.state.errors.driverName}
              value={this.state.form.driverName}
              onChange={this.handleChange}
              name="driverName"
            />
            {this.state.errors.driverName && (
              <Form.Control.Feedback type="invalid">
                {this.state.errors.driverName}
              </Form.Control.Feedback>
            )}
          </Form.Group>

          <Form.Group controlId="ageDriver" size="lg">
            <Form.Label>Driver age</Form.Label>
            <Form.Control
              isInvalid={!!this.state.errors.ageDriver}
              value={this.state.form.ageDriver}
              name="ageDriver"
              onChange={this.handleChange}
              type="number"
            />
            {this.state.errors.ageDriver && (
              <Form.Control.Feedback type="invalid">
                {this.state.errors.ageDriver}
              </Form.Control.Feedback>
            )}
          </Form.Group>

          <Button
            style={{ marginTop: 10 }}
            block="true"
            size="lg"
            type="submit"
          >
            Rent car
          </Button>
        </Form>
        {this.state.summaryCost && (
          <Alert style={{ marginTop: 10 }} variant="success">
            Price: {this.state.summaryCost} SEK
          </Alert>
        )}
      </>
    );
  }
}

export default RentCar;
