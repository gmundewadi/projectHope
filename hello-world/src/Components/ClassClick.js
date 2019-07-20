import React, { Component } from "react";


// this is an example of an event handling for onClick for 
// a class componenent

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
