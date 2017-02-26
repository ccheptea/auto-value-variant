# AutoValue: Variant

An extension for Google's [AutoValue](https://github.com/google/auto/tree/master/value) 
that generates a few handy methods to determine object variations based on statically defined variance groups.
## Common use case

Consider the the following class:
```java
@AutoValue abstract class Car{
    abstract String manufacturer();
    abstract String model();
    abstract String plateNumber;
    abstract int color();
    public abstract String body();
}
```

Having a list of cars and a reference car, lets filter all the cars that have the manufacturer and model fields 
equal to the ones defined in our reference. Most probably we'll end up with some code similar to this:
```java
Car ref = new AutoValue_Car("Tesla", "Model S", "" 0, "");
for(Car car : cars){
    boolean match = (ref.manufacturer() == null ? car.manufacturer() == null : ref.manufacturer().equals(car.manufacturer()))
        && (ref.model() == null ? car.model() == null : ref.model().equals(car.model()))
    
    if(match){
        filtered.add(car);
    }
}
```
This code is hard to read and maintain and can get worse if filters get more complex. Thus we need a better way to 
handle this.

## Variant definition

#### Short 
Two object are _variants_ if they are of the same type, they are not equal, except for a set of their properties.

#### Long
Lets call an object **A** _variant_ of object **B** _iff_ the followings apply:

* **A** and **B** are of the same type
* **A** and **B** have a set **F** of fields that are considered constant
* **A**'s **F** values equal to **B**'s **F** values
* ``A.equals(B)`` returns ``false``
 
## Usage

AutoValue Variant has two key elements:

* ``Variant`` interface, containing the methods for determining variants
* ``@NonVariant`` annotation, applied to fields that we want to consider constant

Lets apply them to our example above.
```java
@AutoValue abstract class Car implements Variant{
    @NonVariant abstract String manufacturer();
    @NonVariant abstract String model();
    abstract String plateNumber;
    abstract int color();
    abstract String body();
}
```

Then filtering will like like this:
```java
for(Car car : cars){
    if(ref.variantOf(car)){
        filtered.add(car);
    }
}

// or lambda

filtered = cars.stream().filter(car -> ref.variantOf(car));
```

## Multiple variant groups
```java
public abstract class CarMultipleGroups implements Variant<CarMultipleGroups> {

    @NonVariant(VariantGroups.IDENTITY)
    public abstract String manufacturer();

    @NonVariant({VariantGroups.IDENTITY, VariantGroups.MODEL_AND_BODY})
    public abstract String model();

    public abstract String plateNumber();

    @NonVariant(VariantGroups.ASPECT)
    public abstract int color();

    @NonVariant({VariantGroups.ASPECT, VariantGroups.MODEL_AND_BODY})
    public abstract String body();

    public static class VariantGroups {
        public static final String IDENTITY = "identity";
        public static final String ASPECT = "aspect";
        public static final String MODEL_AND_BODY = "model_and_body";
    }
}
```
Then apply the ``variantOf(Object, String)`` method

```java
Car ref = new AutoValue_Car("Tesla", "Model S", "", 0xFF000000, "Sedan");

similarTeslaModelSCars = cars.stream().filter(car -> ref.variantOf(car, Car.VariantGroups.IDENTITY));
blackSedans = cars.stream().filter(car -> ref.variantOf(car, Car.VariantGroups.ASPECT));
```

## Download

```groovy
compile 'com.squareup.auto.value:auto-value-variant:1.0.1-SNAPSHOT'
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

## License


```
Copyright 2016 Gabriel Ittner.

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
