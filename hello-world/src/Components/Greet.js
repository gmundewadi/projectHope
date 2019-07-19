import React from "react";

// if it possible to create a functional component
// you should do so! Because you dont need to use the
// this or state keyword. functional compnents

// you can add export up from of this line
// but then you need to import this file
// with its name in App.js

const Greet = ({ name, heroName }) => {
  console.log(name);
  return (
    <div>
      <h1>
        Hello {name} a.k.a {heroName}
      </h1>
    </div>
  );
};

// The line of code above is the same as the functional
// component below
// function Greet() {
//   return <h1>Hello Gautam</h1>;
// }

export default Greet;
// you export as a default
// allowing you to change the name
// of the import in the App.js file
// and import this functin there
