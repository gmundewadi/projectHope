import React, { Component } from "react";

export class ClassClick extends Component {
  clickHandler() {
    console.log("Clicked the button");
  }
  render() {
    // REMEMBER: in a method. functions are refered to using the
    // this keyword!
    return (
      <div>
        <button onClick={this.clickHandler}> Click Me </button>
      </div>
    );
  }
}

export default ClassClick;
