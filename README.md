# AutoValue: Variant Extension [![Build Status](https://travis-ci.org/ccheptea/auto-value-variant.svg?branch=master)](https://travis-ci.org/ccheptea/auto-value-variant)

An extension for Google's [AutoValue](https://github.com/google/auto/tree/master/value) 
that generates a few handy methods to determine object variations based on statically defined variance groups.

## The problem

Consider the following class:
```java
@AutoValue abstract class Car{
    abstract String manufacturer();
    abstract String model();
    abstract String plateNumber();
    abstract int color();
    abstract String body();
}
```

Having a list of cars and a reference car, lets filter all the cars that have the manufacturer and model fields 
equal to the ones defined in our reference. Most probably we'll end up with some code similar to this:
```java
Car ref = new AutoValue_Car("Tesla", "Model S", "", 0, "");
for(Car car : cars){
    boolean match = (ref.manufacturer() == null ? car.manufacturer() == null : ref.manufacturer().equals(car.manufacturer()))
        && (ref.model() == null ? car.model() == null : ref.model().equals(car.model()))
    
    if(match){
        filtered.add(car);
    }
}
```
This code is hard to read and maintain and can get worse if filters get more complex.

## Variant definition

#### Short 
Two objects are _variants_ if they are of the same type and are not equal except for a set of their properties.

#### Long
Lets call an object **A** _variant_ of object **B** if the followings apply:

* **A** and **B** are of the same type
* **A** and **B** have a set **F** of fields that are considered constant
* Each property from **F(A)** equals to its corresponding property in **F(B)**
* ``A.equals(B)`` returns ``false``
 
## Usage

AutoValue Variant has two key elements:

* ``Variant`` interface, containing the methods for determining variants
* ``@NonVariant`` annotation, applied to fields that we want to consider constant

Implement ``Variant``, then apply the ``@NonVariant`` annotation on fields you want to consider constant. 
```java
@AutoValue abstract class Car implements Variant{
    @NonVariant abstract String manufacturer();
    @NonVariant abstract String model();
    abstract String plateNumber();
    abstract int color();
    abstract String body();
}
```

Then filtering will look like this:
```java
for(Car car : cars){
    if(ref.like(car)){
        filtered.add(car);
    }
}
// or 
filtered = cars.stream().filter(car -> ref.like(car));
```

## Multiple variant groups
You can define multiple variant groups by specifying the group name when applying the ``@NonVariant`` annotation

```java
public abstract class Car implements Variant<Car> {

    @NonVariant(VariantGroups.IDENTITY)
    abstract String manufacturer();

    @NonVariant({VariantGroups.IDENTITY, VariantGroups.MODEL_AND_BODY})
    abstract String model();

    abstract String plateNumber();

    @NonVariant(VariantGroups.ASPECT)
    abstract int color();

    @NonVariant({VariantGroups.ASPECT, VariantGroups.MODEL_AND_BODY})
    abstract String body();

    static class VariantGroups {
        static final String IDENTITY = "identity";
        static final String ASPECT = "aspect";
        static final String MODEL_AND_BODY = "model_and_body";
    }
}
```
Then apply the ``like(Object, String)`` method

```java
Car ref = new AutoValue_Car("Tesla", "Model S", "", 0xFF000000, "Sedan");

similarTeslaModelSCars = cars.stream().filter(car -> ref.like(car, Car.VariantGroups.IDENTITY));
blackSedans = cars.stream().filter(car -> ref.like(car, Car.VariantGroups.ASPECT));
```

## Download

```groovy
compile 'com.ccheptea.auto.value:auto-value-variant:1.0.1-SNAPSHOT'
 ```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].


## Notes

The code for auto deploying artifacts with Travis as well as some of the extension code is based on or inspired by parts of:
* [AutoValue: Redacted Extension] (https://github.com/square/auto-value-redacted)
* [AutoValue: Parcel Extension] (https://github.com/rharter/auto-value-parcel)

## License


```
Copyright 2017 Constantin Cheptea.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[snap]: https://oss.sonatype.org/content/repositories/snapshots/
