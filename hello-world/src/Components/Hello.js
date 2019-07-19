import React from "react";

const Hello = () => {
  return (
    <div className="dummyClass">
      <h1>Hello Gautam</h1>
    </div>
  );
  // Stuff below is what the code above does
  // such a waste of time use the code above
  // return React.createElement(
  //   "div",
  //   { id: "hello", className: "dummyClass" },
  //   React.createElement("h1", null, "Hello Gautam")
  // );
};

export default Hello;
