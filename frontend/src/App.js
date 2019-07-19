import React from "react";

export default class FetchRSSFeed extends React.Component {
  state = {
    loading: true,
    data: []
  };

  async componentDidMount() {
    const url = "http://localhost:8080/articles/";
    const response = await fetch(url);
    const data = await response.json();
    this.setState({ loading: false });
    this.setState({ data: data });
    console.log(data);
  }

  render() {
    return (
      <div>
        {this.state.data.map((dynamicData, key) => (
          <div>
            <a href={dynamicData.uri}>
              <b> {dynamicData.title}</b>
            </a>
            <p>
              <img
                src={dynamicData.image}
                width="500"
                height="300"
                alt="image did not load"
              />
            </p>
            <p> {dynamicData.uri}</p>
            <p> {dynamicData.description}</p>
          </div>
        ))}
      </div>
    );
  }
}
