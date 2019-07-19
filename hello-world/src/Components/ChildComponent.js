import React from "react";

// example of childCompnent calling a method
// in the parentCompnenet. In this case
// greetParent. Passing prop back to parent component

// Arrow function syntax passes parameter back
// to the parent Component

function ChildComponent(props) {
  return (
    <div>
      <button onClick={() => props.greetHandler("Gautam")}>Greet Parent</button>
    </div>
  );
}

export default ChildComponent;
