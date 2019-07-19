import React, { Component } from "react";
import ChildComponent from "./ChildComponent";

export class ParentComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      parentName: "Parent"
    };
    this.greetParent = this.greetParent.bind(this);
  }

  // this print statement alerts user as a popup. The
  // weird way of outputting can be ignored and you can replace it
  // with regular Hello + ...
  greetParent(childName) {
    alert(`Hello ${this.state.parentName} from ${childName}`);
  }

  render() {
    // passing the reference to the greetParent method
    // as a prop to the ChildComponent
    return (
      <div>
        <ChildComponent greetHandler={this.greetParent} />
      </div>
    );
  }
}

export default ParentComponent;
