import React from "react";
import { Component } from "react";

// class components can maintain thier own private data
// capable of complex UI logic
// also called Stateful/Smart/Container components

// class needs to extend component
// in order to work with react. Must also
// include a render method

// poperties are avliable using this.props in class
// components. props are immutable
class Welcome extends Component {
  render() {
    const { name, heroName } = this.props;
    return (
      <h1>
        Welcome {name} a.k.a {heroName}
      </h1>
    ); // this is JSX
  }
}

export default Welcome;
