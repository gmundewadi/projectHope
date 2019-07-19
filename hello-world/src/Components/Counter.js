import React from "react";
import { Component } from "react";

class Counter extends Component {
  constructor() {
    super();
    this.state = {
      count: 0
    };
  }

  increment() {
    // Use this code when your current state
    // relies on the previous state
    this.setState(previousState => ({
      count: previousState.count + 1
    }));

    // this.setState(
    //   {
    //     count: this.state.count + 1
    //   },
    //   () => {
    //     console.log(this.state.count);
    // this second parameter updates
    // console.log at the sync to this.setState
    // whenever you need to execute code AFTER
    // setStat method, place that code within the
    // callback function like above. USE
    // funciton pointers here
    //   }
    // );
    // call to setState are async.
    // so console.log() runs before console.log:
    // console.log(this.state.count);
  }

  incrementFive() {
    this.increment();
    this.increment();
    this.increment();
    this.increment();
    this.increment();
  }

  render() {
    return (
      <div>
        <div>Count {this.state.count}</div>
        <button onClick={() => this.incrementFive()}> Increment</button>
      </div>
    );
  }
}

export default Counter;
