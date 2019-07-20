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
    const url = "http://localhost:8080/articles/";
    const response = await fetch(url);
    const data = await response.json();
    this.setState({ articles: data });
  }

  render() {
    return (
      <div>
        {this.state.articles.map((data, index) => (
          <div className="row">
            <div className="column">
              <p>
                <a href={data.uri}>
                  <b>{data.title}</b>
                </a>
              </p>
              <img
                src={data.image}
                widgth="200"
                height="200"
                alt="image did not load"
              />
              <p>{data.description}</p>
            </div>
          </div>
        ))}
      </div>

      // <div className="row">
      //   <div className="column">
      //     <h2>Column 1</h2>
      //     <p>Some text..</p>
      //   </div>
      //   <div className="column">
      //     <h2>Column 2</h2>
      //     <p>Some text..</p>
      //   </div>
      // </div>
    );
  }
}
export default Fetch;
