# Serializer
A simple serializer and deserializer for CPSC 501. See the [assignment specifications](Assignment3.pdf) for more details.

This project uses the `AsciiPanel` library for user input in a text-based interface. This library can be found here: [AsciiPanel](https://github.com/trystan/AsciiPanel).

### Process
 - `Object Creator` to create arbitrary objects through text-based user input.
 - These objects are sent to the `Serializer` and are serialized using the JDOM library.
 - The resulting JDOM document are sent as a byte stream over the network using sockets, to be received by the `Deserializer`.
 - The `Deserializer` deserialized objects are then passed to an `Object Inspector` to view the contents.

### Object Types
 - `Primitive`: Contains fields for each of Java's primitive types
 - `Reference`: Contains references to up to 3 other objects that have already been created by the user
 - `Primitive Array`: Contains an array of arbitrary length of primitives
 - `Reference Array`: Contains an array of arbitrary length of other objects that have been created by the user
 - `ArrayList`: Contains a `java.util.ArrayList` that refers to other objects created by the user

### Usage
 - Open two separate instances of the program through the command line (`java application.Main`)
 - With one instance, create one or more objects through the `Object Creator` terminal
 - The other instance will open the `Receive Object` terminal and enter `Receive Object` mode
 - The first instance can then open the `Serialize` terminal, serialize the object that they wish to use and send it to the receiving instance.
 - Once the receiving instance gets the object it will automatically deserialize it. The deserialized object can then be viewed in the `Object Inspector`
