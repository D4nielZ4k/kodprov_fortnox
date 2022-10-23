import moment from "moment";

function getCurrentDate() {
  const now = moment().format("yyyy-MM-DD");

  return now;
}

export default getCurrentDate;
