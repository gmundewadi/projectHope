import React from "react";

export default class FetchRSSFeed extends React.Component {
  state = {
    loading: true
  };

  async componentDidMount() {
    const url = "http://localhost:8080/articles/";
    const response = await fetch(url);
    const data = await response.json();
    console.log(data);
  }

  render() {
    return (
      <div>
        {this.state.loading ? <div> loading... </div> : <div>person...</div>}
      </div>
    );
  }
}
