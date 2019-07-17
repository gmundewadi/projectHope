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
            <p>
              <b>TITLE: {dynamicData.title}</b>
            </p>
            <p>PUBDATE: {dynamicData.pubDate}</p>
            <p>URL: {dynamicData.uri}</p>
            <p>DESCRIPTION: {dynamicData.description}</p>
          </div>
        ))}
      </div>
    );
  }
}
