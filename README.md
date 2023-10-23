# Object Inspector
A simple serializer and deserializer for CPSC 501. See the [assignment specifications](Assignment3.pdf) for more details.

### Process
 - `Object Creator` to create arbitrary objects through text-based user input.
 - These objects are sent to the `Serializer` and are serialized using the JDOM library.
 - The resulting JDOM document are sent as a byte stream over the network using sockets, to be received by the `Deserializer`.
 - The `Deserializer` deserialized objects are then passed to an `Object Inspector` to view the contents.
