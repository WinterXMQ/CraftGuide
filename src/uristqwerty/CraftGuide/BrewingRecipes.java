package uristqwerty.CraftGuide;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.PotionHelper;
import net.minecraft.src.mod_CraftGuide;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.gui.texture.DynamicTexture;
import uristqwerty.gui.texture.TextureClip;

public class BrewingRecipes extends CraftGuideAPIObject implements RecipeProvider
{
	private final Slot[] slots = new ItemSlot[]{
		new ItemSlot(12, 12, 16, 16).setSlotType(SlotType.INPUT_SLOT),
		new ItemSlot(12, 30, 16, 16).setSlotType(SlotType.INPUT_SLOT),
		new ItemSlot(49, 21, 16, 16).setSlotType(SlotType.OUTPUT_SLOT),
	};

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		ItemStack stack = new ItemStack(Item.brewingStand);
		List<ItemStack[]> recipes = getRecipes();
		/*RecipeTemplate template = generator.createRecipeTemplate(slots, stack,
			"/gui/BrewGuide.png", 1, 1, 82, 1);*/

		RecipeTemplate template = new DefaultRecipeTemplate(
				slots,
				stack,
				new TextureClip(
						DynamicTexture.instance("brew_recipe_background"),
						1, 1, 79, 58),
				new TextureClip(
						DynamicTexture.instance("brew_recipe_background"),
						82, 1, 79, 58));

		if(mod_CraftGuide.hideMundanePotionRecipes)
		{
			Iterator<ItemStack[]> iterator = recipes.iterator();

			while(iterator.hasNext())
			{
				ItemStack[] recipe = iterator.next();

				if(recipe[2] != null && recipe[2].getItemDamage() == 8192)
				{
					iterator.remove();
				}
			}
		}

		for(ItemStack[] recipe: recipes)
		{
			generator.addRecipe(template, recipe);
		}

		generator.setDefaultTypeVisibility(stack, false);
	}

	private List<ItemStack[]> getRecipes()
	{
		List<Item> ingredients = getIngredients();

		ItemStack water = new ItemStack(Item.potion);
		List<ItemStack[]> potionRecipes = new LinkedList<ItemStack[]>();
		Set<Integer> done = new HashSet<Integer>();
		done.add(0);

		addRecipesForPotion(potionRecipes, water, ingredients, done);

		return potionRecipes;
	}

	private void addRecipesForPotion(List<ItemStack[]> potionRecipes, ItemStack potion, List<Item> ingredients, Set<Integer> done)
	{
		List<ItemStack> next = new LinkedList<ItemStack>();

		for(Item ingredient: ingredients)
		{
			int result = PotionHelper.applyIngredient(potion.getItemDamage(), ingredient.getPotionEffect());

			if(result != 0 && result != potion.getItemDamage())
			{
				ItemStack output = new ItemStack(Item.potion);
				output.setItemDamage(result);
				potionRecipes.add(new ItemStack[] {potion, new ItemStack(ingredient), output});

				if(!done.contains(result))
				{
					next.add(output);
					done.add(result);
				}
			}
		}

		for(ItemStack nextPotion: next)
		{
			addRecipesForPotion(potionRecipes, nextPotion, ingredients, done);
		}
	}

	private List<Item> getIngredients()
	{
		List<Item> ingredients = new LinkedList<Item>();

		for(Item item: Item.itemsList)
		{
			if(item != null && item.isPotionIngredient())
			{
				ingredients.add(item);
			}
		}

		return ingredients;
	}
}
