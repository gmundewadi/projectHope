import React from "react";
import logo from "./logo.svg";
import "./App.css";
import Greet from "./Components/Greet";
import Welcome from "./Components/Welcome";
import Message from "./Components/Message";
import Hello from "./Components/Hello";
import Counter from "./Components/Counter";
import FunctionClick from "./Components/FunctionClick";
import ClassClick from "./Components/ClassClick";

function App() {
  return (
    <div className="App">
      <FunctionClick />
      <ClassClick />
      {/* <Counter /> */}
      {/* <Greet name="Bruce" heroName="Batman">
        <p>This is children props</p>
      </Greet>
      <Greet name="Clark" heroName="Superman">
        <button>Action</button>
      </Greet> */}
      {/* <Greet name="Diana" heroName="Wonder Woman" />
      <Welcome name="Bruce" heroName="Batman" /> */}
      {/* <Welcome name="Clark" heroName="Superman" />
      <Welcome name="Diana" heroName="Wonder Woman" />  */}{" "}
    </div>
  );
}

export default App;
