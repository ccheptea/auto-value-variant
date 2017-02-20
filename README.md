# AutoValue: Variant

An extension for Google's [AutoValue](https://github.com/google/auto/tree/master/value) 
that generates a method to determine if an object is a variation of another object of the same type.

## Simple Use case

Consider the the following class:
````
class Car{
    String manufacturer;
    String model;
    int color;
    String plateNumber;
}
````

A car cannot change its manufacturer or model, but it can 
change its color or plate number. If you where to look for all cars similar 
(but not identical) to a Tesla Model S in list you would need to compare both 
manufacturer and model fields then make sure ``equals`` returns false.

A simpler case is when you need to check if an objects coming from a server needs to be updated
locally. The flow is the same, check the ids then make sure the rest is not equal.

#### Solution

Write a method that implements the flow described above.

## Usage

Implement 
