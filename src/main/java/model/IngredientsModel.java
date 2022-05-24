package model;

import java.util.List;

public class IngredientsModel {

    private boolean success;
    private List<IngredientModel> data;

    public IngredientsModel() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<IngredientModel> getData() {
        return data;
    }

    public void setIngredients(List<IngredientModel> data) {
        this.data = data;
    }
}
