package uristqwerty.CraftGuide.server;

import net.minecraft.src.EntityPlayer;
import uristqwerty.CraftGuide.CraftGuideSide;
import uristqwerty.CraftGuide.api.Util;

public class CraftGuideServer implements CraftGuideSide
{
	@Override
	public void initKeybind()
	{
	}

	@Override
	public void preInit()
	{
		Util.instance = new UtilImplementationServer();
	}

	@Override
	public void reloadRecipes()
	{
	}

	@Override
	public void openGUI(EntityPlayer player)
	{
	}
}