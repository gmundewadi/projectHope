import React, { Component } from "react";
import "./Article.css";

export class Fetch extends Component {
  constructor(props) {
    super(props);
    this.state = {
      articles: []
    };
  }

  async componentDidMount() {
    const url = "http://localhost:8080/articles/query/100";
    const response = await fetch(url);
    const data = await response.json();
    this.setState({ articles: data });
  }

  render() {
    return (
      <div>
        <h1 className="title"> Project Hope </h1>
        {this.state.articles.map((data, index) => (
          <div className="row">
            <div className="column">
              <p className="titlefont">
                <a href={data.url} target="_blank">
                  <b>{data.title}</b>
                </a>
              </p>
              <img
                src={data.image}
                widgth="100"
                height="100"
                alt="image did not load"
              />
            </div>
          </div>
        ))}
      </div>
    );
  }
}
export default Fetch;
