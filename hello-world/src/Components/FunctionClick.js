import React from "react";

// this is an example of an event handling for onClick for
// a functional componenent

function FunctionClick() {
  function clickHandler() {
    console.log("Button Clicked");
  }
  return (
    // event handler is a function not a function call!
    <div>
      <button onClick={clickHandler}>Click</button>
    </div>
  );
}

export default FunctionClick;
