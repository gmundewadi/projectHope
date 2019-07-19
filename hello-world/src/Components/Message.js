import React from "react";
import { Component } from "react";

class Message extends Component {
  constructor() {
    super();
    this.state = {
      message: "Welcome Visitor"
    };
  }

  changeMessage() {
    this.setState({
      message: "Thank you for subcribing"
    });
  }

  render() {
    return (
      <div>
        <h1>{this.state.message}</h1>
        <button onClick={() => this.changeMessage()}>Subsribe</button>
      </div>
    ); // code in return is JSX
  }
}

export default Message;
