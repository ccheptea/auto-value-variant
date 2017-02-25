package com.ccheptea.auto.value.variant;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by constantin.cheptea
 * on 24/02/2017.
 */
public class AutoValueVariantFunctionalityTest {

    List<CarSingleGroup> carsWithSingleGroup = new ArrayList<>();
    List<CarMultipleGroups> carsWithMultipleGroups = new ArrayList<>();
    List<CarNoGroup> carsWithNoGroup = new ArrayList<>();
    List<CarDefaultGroup> carsWithDefaultGroup = new ArrayList<>();

    @Before
    public void setup() {
        carsWithSingleGroup.add(createCarSingleGroup("Tesla", "Model S", "PLATE1", 555, "Sedan"));
        carsWithSingleGroup.add(createCarSingleGroup("Tesla", "Model S", "PLATE2", 777, "Pickup"));
        carsWithSingleGroup.add(createCarSingleGroup("Tesla", "Model X", "PLATE3", 643, "Hatchback"));
        carsWithSingleGroup.add(createCarSingleGroup("Tesla", "Model X", "PLATE4", 777, "Hatchback"));
        carsWithSingleGroup.add(createCarSingleGroup("KIA", "Sorento", "PLATE5", 989, "SUV"));
        carsWithSingleGroup.add(createCarSingleGroup("Nissan", "Pathfinder", "PLATE6", 555, "SUV"));

        carsWithMultipleGroups.add(createCarMultipleGroups("Tesla", "Model S", "PLATE1", 555, "Sedan"));
        carsWithMultipleGroups.add(createCarMultipleGroups("Tesla", "Model S", "PLATE2", 777, "Pickup"));
        carsWithMultipleGroups.add(createCarMultipleGroups("Tesla", "Model X", "PLATE3", 643, "Hatchback"));
        carsWithMultipleGroups.add(createCarMultipleGroups("Tesla", "Model X", "PLATE4", 777, "Hatchback"));
        carsWithMultipleGroups.add(createCarMultipleGroups("KIA", "Sorento", "PLATE5", 555, "SUV"));
        carsWithMultipleGroups.add(createCarMultipleGroups("Nissan", "Pathfinder", "PLATE6", 555, "SUV"));
        carsWithMultipleGroups.add(createCarMultipleGroups("Tesla", "Model X", "PLATE6", 555, "Sedan"));

        carsWithNoGroup.add(createCarNoGroup("Tesla", "Model S", "PLATE1", 555, "Sedan"));
        carsWithNoGroup.add(createCarNoGroup("Tesla", "Model S", "PLATE2", 777, "Pickup"));


        carsWithDefaultGroup.add(createCarDefaultGroup("Tesla", "Model S", "PLATE1", 555, "Sedan"));
        carsWithDefaultGroup.add(createCarDefaultGroup("Tesla", "Model S", "PLATE2", 777, "Pickup"));
    }

    @Test
    public void testMultipleGroups() {
        CarMultipleGroups refIdentity = createCarMultipleGroups("Tesla", "Model S", "", 0, "");
        CarMultipleGroups refModelBody = createCarMultipleGroups("", "Model X", "", 0, "Hatchback");
        CarMultipleGroups refAspect = createCarMultipleGroups("", "", "", 555, "Sedan");

        List<CarMultipleGroups> identityVariants = new ArrayList<>();
        List<CarMultipleGroups> modelBodyVariants = new ArrayList<>();
        List<CarMultipleGroups> aspectVariants = new ArrayList<>();

        List<CarMultipleGroups> expectedIdentityVariants = new ArrayList<>();
        List<CarMultipleGroups> expectedModelBodyVariants = new ArrayList<>();
        List<CarMultipleGroups> expectedAspectVariants = new ArrayList<>();

        expectedIdentityVariants.add(carsWithMultipleGroups.get(0));
        expectedIdentityVariants.add(carsWithMultipleGroups.get(1));

        expectedModelBodyVariants.add(carsWithMultipleGroups.get(2));
        expectedModelBodyVariants.add(carsWithMultipleGroups.get(3));

        expectedAspectVariants.add(carsWithMultipleGroups.get(0));
        expectedAspectVariants.add(carsWithMultipleGroups.get(6));

        for (CarMultipleGroups car : carsWithMultipleGroups) {
            if (refIdentity.variantOf(car, CarMultipleGroups.VariantGroups.IDENTITY)) {
                identityVariants.add(car);
            }

            if (refModelBody.variantOf(car, CarMultipleGroups.VariantGroups.MODEL_AND_BODY)) {
                modelBodyVariants.add(car);
            }

            if (refAspect.variantOf(car, CarMultipleGroups.VariantGroups.ASPECT)) {
                aspectVariants.add(car);
            }
        }

        assert identityVariants.equals(expectedIdentityVariants);
        assert modelBodyVariants.equals(expectedModelBodyVariants);
        assert aspectVariants.equals(expectedAspectVariants);
    }

    @Test
    public void testSingleGroup() {
        CarSingleGroup ref = createCarSingleGroup("Tesla", "Model S", "", 0, "");

        List<CarSingleGroup> expected = new ArrayList<>();
        List<CarSingleGroup> result = new ArrayList<>();

        expected.add(carsWithSingleGroup.get(0));
        expected.add(carsWithSingleGroup.get(1));

        for (CarSingleGroup car : carsWithSingleGroup) {
            if (ref.variantOf(car, CarSingleGroup.VariantGroups.IDENTITY)) {
                result.add(car);
                System.out.println(car.toString());
            }
        }

        assert result.equals(expected);
    }

    @Test
    public void testDefaultGroup() {
        CarDefaultGroup ref = createCarDefaultGroup("Tesla", "Model S", "", 0, "");

        List<CarDefaultGroup> expected = new ArrayList<>();
        List<CarDefaultGroup> result = new ArrayList<>();

        expected.add(carsWithDefaultGroup.get(0));
        expected.add(carsWithDefaultGroup.get(1));

        for (CarDefaultGroup car : carsWithDefaultGroup) {
            if (ref.variantOf(car)) {
                result.add(car);
            }
        }

        assert result.equals(expected);
    }

    @Test
    public void testNoGroup() {
        CarNoGroup ref = createCarNoGroup("Tesla", "Model S", "PLATE1", 555, "Sedan");

        List<CarNoGroup> variantsOnly = new ArrayList<>();
        List<CarNoGroup> resultVariantsOnly = new ArrayList<>();

        List<CarNoGroup> variantsOrEquals = new ArrayList<>();
        List<CarNoGroup> resultVariantsOrEquals = new ArrayList<>();

        variantsOrEquals.addAll(carsWithNoGroup);
        variantsOnly.add(carsWithNoGroup.get(1));

        for (CarNoGroup car : carsWithNoGroup) {
            if (ref.variantOf(car)) {
                resultVariantsOnly.add(car);
            }
            if (ref.variantOrEqual(car)) {
                resultVariantsOrEquals.add(car);
            }
        }

        assert variantsOnly.equals(resultVariantsOnly);
        assert variantsOrEquals.equals(resultVariantsOrEquals);
    }

    private static CarMultipleGroups createCarMultipleGroups(String manufacturer, String model, String plate, int color, String body) {
        return new com.ccheptea.auto.value.variant.AutoValue_CarMultipleGroups(manufacturer, model, plate, color, body);
    }

    private static CarNoGroup createCarNoGroup(String manufacturer, String model, String plate, int color, String body) {
        return new com.ccheptea.auto.value.variant.AutoValue_CarNoGroup(manufacturer, model, plate, color, body);
    }

    private static CarDefaultGroup createCarDefaultGroup(String manufacturer, String model, String plate, int color, String body) {
        return new com.ccheptea.auto.value.variant.AutoValue_CarDefaultGroup(manufacturer, model, plate, color, body);
    }

    private static CarSingleGroup createCarSingleGroup(String manufacturer, String model, String plate, int color, String body) {
        return new com.ccheptea.auto.value.variant.AutoValue_CarSingleGroup(manufacturer, model, plate, color, body);
    }

}
