package thetadev.constructionwand.integrations.jei;

import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thetadev.constructionwand.ConstructionWand;
import thetadev.constructionwand.basics.ConfigClient;
import thetadev.constructionwand.basics.ConfigServer;
import thetadev.constructionwand.items.ModItems;

import javax.annotation.Nonnull;

@JeiPlugin
public class ConstructionWandJeiPlugin implements IModPlugin
{
    private static final ResourceLocation pluginId = new ResourceLocation(ConstructionWand.MODID, ConstructionWand.MODID);
    private static final String baseKey = ConstructionWand.MODID + ".description.";
    private static final String baseKeyItem = "item." + ConstructionWand.MODID + ".";

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return pluginId;
    }

    private Component keyComboComponent(boolean shiftOpt, Component optkeyComponent) {
        String key = shiftOpt ? "sneak_opt" : "sneak";
        return new TranslatableComponent(baseKey + "key." + key, optkeyComponent).withStyle(ChatFormatting.BLUE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Component optkeyComponent = new TranslatableComponent(InputConstants.getKey(ConfigClient.OPT_KEY.get(), -1).getName())
                .withStyle(ChatFormatting.BLUE);
        Component wandModeComponent = keyComboComponent(ConfigClient.SHIFTOPT_MODE.get(), optkeyComponent);
        Component wandGuiComponent = keyComboComponent(ConfigClient.SHIFTOPT_GUI.get(), optkeyComponent);

        for(Item wand : ModItems.WANDS) {
            ConfigServer.WandProperties wandProperties = ConfigServer.getWandProperties(wand);

            String durabilityKey = wand == ModItems.WAND_INFINITY ? "unlimited" : "limited";
            Component durabilityComponent = new TranslatableComponent(baseKey + "durability." + durabilityKey, wandProperties.getDurability());

            registration.addIngredientInfo(new ItemStack(wand), VanillaTypes.ITEM,
                    new TranslatableComponent(baseKey + "wand",
                            new TranslatableComponent(baseKeyItem + wand.getRegistryName().getPath()),
                            wandProperties.getLimit(), durabilityComponent,
                            optkeyComponent, wandModeComponent, wandGuiComponent)
            );
        }

        for(Item core : ModItems.CORES) {
            registration.addIngredientInfo(new ItemStack(core), VanillaTypes.ITEM,
                    new TranslatableComponent(baseKey + core.getRegistryName().getPath()),
                    new TranslatableComponent(baseKey + "core", wandModeComponent)
            );
        }
    }
}