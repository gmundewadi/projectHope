import React, { Component } from "react";

export class EventBind extends Component {
  constructor(props) {
    super(props);

    this.state = {
      message: "Hello"
    };

    this.clickHandler = this.clickHandler.bind(this);
  }

  clickHandler() {
    console.log(this);
    this.setState({
      message: "Goodbye!"
    });
  }

  render() {
    // Binding in render PERFORMACE IMPLICATIONS
    // Arrow function in Render PERFORMCE IMPLICATIONS
    // BEST: Binding in constructor (See constructor) <----
    // clickHandler() function into arrow function
    return (
      <div>
        <div>{this.state.message}</div>
        {/* <button onClick={this.clickHandler.bind(this)}>Click</button> */}
        {/* <button onClick={() => this.clickHandler()}>Click</button> */}
        <button onClick={this.clickHandler}>Click</button>
      </div>
    );
  }
}

export default EventBind;
