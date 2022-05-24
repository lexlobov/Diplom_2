package model;

import java.util.List;

public class IngredientsModel {

    private boolean success;
    private List<IngredientModel> ingredients;

    public IngredientsModel() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }
}
