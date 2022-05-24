package model;

import java.util.List;

public class IngredientsCreateModel {

    private List<IngredientCreateModel> ingredients;

    public IngredientsCreateModel() {
    }

    public List<IngredientCreateModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientCreateModel> ingredients) {
        this.ingredients = ingredients;
    }
}
