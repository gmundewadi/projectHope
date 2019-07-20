import React from "react";

// JSX Differences

// Class -> className
// for -> htmlFor
// onclick -> onClick


const Hello = () => {
  return (
    <div className="dummyClass">
      <h1>Hello Gautam</h1>
    </div>
  );
  // Stuff below is what the code above does
  // such a waste of time use the JSX code above
  // return React.createElement(
  //   "div",
  //   { id: "hello", className: "dummyClass" },
  //   React.createElement("h1", null, "Hello Gautam")
  // );
};

export default Hello;
